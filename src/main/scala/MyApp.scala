import java.time.{Duration, LocalTime}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import BusTimes.createNextBusTimeElement
import org.scalajs.dom.document
import org.scalajs.dom.html.{Anchor, Div}
import org.scalajs.dom.raw.{HTMLElement, Node}
import scalatags.JsDom
import zio.{App, ZIO}
import zio.console.{putStrLn, _}
import zio.clock._

object MyApp extends App {

  override def run(
    args: List[String]
  ): ZIO[zio.ZEnv, Nothing, Int] = {
    val myEnvironment
      : zio.clock.Clock with zio.console.Console with Browser =
      new Clock.Live with Console.Live with BrowserLive

    (for {
      _ <- DomManipulation.createAndApplyPageStructure
      _ <- addAllBusTimesToPage // This only executes once.
      /* Looping behavior for page:
      _   <- ZIO {  eventLoop } */
    } yield { 0 }).provide(myEnvironment)
  }

  val addAllBusTimesToPage
    : ZIO[Browser with Clock with Console, Nothing, Unit] = {
    import scalatags.JsDom.all._ // TODO WHAT AN OBVIOUS WART!!!
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      localTime = now.toLocalTime
      _ <- DomManipulation.addDivToUpcomingBusesSection(
        div(
          createNextBusTimeElement(BusTimes.oldTownHallBusStarts,
                                   localTime),
          createNextBusTimeElement(BusTimes.clarksBusStarts,
                                   localTime),
          createNextBusTimeElement(BusTimes.fourWayUphillBusStarts,
                                   localTime),
          createNextBusTimeElement(BusTimes.teocalliUphillBusStarts,
                                   localTime),
          createNextBusTimeElement(
            BusTimes.mountaineerSquareBusStarts,
            localTime
          ),
          createNextBusTimeElement(BusTimes.teocalliDownhillBusStarts,
                                   localTime),
          createNextBusTimeElement(BusTimes.fourwayDownhill,
                                   localTime)
        )
      )
    } yield ()
  }

}

object StopLocation extends Enumeration {
  protected case class Val(name: String) extends super.Val(name)
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  val OldTownHall: Val = Val("Old Town Hall")
  val Clarks: Val = Val("6th/Belleview (Clarks)")
  val FourWayUphill: Val = Val("4-way (To Mountain)")
  val TeocalliUphill: Val = Val("Teocalli (To Mountain)")
  val MountaineerSquare: Val = Val("Mountaineer Square")
  val TeocalliDownhill: Val = Val("Teocalli (To Downtown)")
  val FourwayDownhill: Val = Val("4-way (To Downtown)")
}

object BusTimes {
  /*
    Regular Winter Schedule: November 27th through April 12th

    Every 15 Minutes from 7:10 AM to Midnight

                          Stop	Times	            First Bus	  Last Bus
    Old Town Hall 	      :10, :25, :40, :55	    7:10 AM	    11:40 PM
    6th and Belleview	    :59, :14, :29, :44	    7:14 AM	    11:44 PM
    4-Way Stop	          :00, :15, :30, :45 	    7:15 AM	    11:45 PM
    Teocalli	            :01, :16, :31, :46	    7:16 AM	    11:46 PM
    Mountaineer Square	  :00, :15, :30, :45	    7:30 AM 	  12:00 AM
   */

  private val startTime = LocalTime.parse("07:10:00")
  private val endTime = LocalTime.parse("23:40:00")

  private val totalBusRunTime =
    java.time.Duration.between(startTime, endTime)

  private val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration
      .ofMinutes(15)
      .getSeconds

  val oldTownHallBusStarts: Stops =
    Stops(StopLocation.OldTownHall,
          List
            .range(0, numberOfBusesPerDay)
            .map(
              index =>
                startTime.plus(
                  java.time.Duration.ofMinutes(15).multipliedBy(index)
                )
            ))

  val clarksBusStarts: Stops =
    Stops(StopLocation.Clarks,
          oldTownHallBusStarts.times
            .map(_.plusMinutes(4)))

  val fourWayUphillBusStarts: Stops =
    Stops(StopLocation.FourWayUphill,
          clarksBusStarts.times
            .map(_.plusMinutes(1)))

  val teocalliUphillBusStarts: Stops =
    Stops(StopLocation.TeocalliUphill,
          fourWayUphillBusStarts.times
            .map(_.plusMinutes(1)))

  val mountaineerSquareBusStarts: Stops =
    Stops(StopLocation.MountaineerSquare,
          teocalliUphillBusStarts.times
            .map(_.plusMinutes(14)))

  val teocalliDownhillBusStarts: Stops =
    Stops(StopLocation.TeocalliDownhill,
          mountaineerSquareBusStarts.times
            .map(_.plusMinutes(6)))

  val fourwayDownhill: Stops =
    Stops(StopLocation.FourwayDownhill,
          teocalliDownhillBusStarts.times
            .map(_.plusMinutes(1)))

  val printBusInfo: ZIO[Console, Nothing, Unit] =
    for {
      _ <- putStrLn("First Bus: " + BusTimes.startTime)
      _ <- putStrLn("Last Bus: " + BusTimes.endTime)
      _ <- putStrLn("Total Bus Run Time:" + BusTimes.totalBusRunTime)
      _ <- putStrLn(
        "Number of buses per day:" + BusTimes.numberOfBusesPerDay
      )
    } yield ()

  def nextBusArrivalTime(timesAtStop: Seq[LocalTime],
                         localTime: LocalTime): Option[LocalTime] =
    localTime match {
      case localTime: LocalTime
          if localTime.isAfter(LocalTime.parse("04:00:00")) =>
        timesAtStop
          .dropWhile(
            stopTime =>
              stopTime
                .isBefore(localTime.truncatedTo(ChronoUnit.MINUTES))
          )
          .headOption
      case _ => Option.empty
    }

  def nextBusTime(stops: Stops, localTime: LocalTime): NextStop = // TODO use ZIO.option
    BusTimes
      .nextBusArrivalTime(stops.times, localTime)
      .map(
        nextArrivalTime =>
          NextStop(stops.location, Left(nextArrivalTime))
      )
      .getOrElse(
        NextStop(
          stops.location,
          Right(SafeRideRecommendation("safe-ride"))
        )
      )

  def createNextBusTimeElement(
    stops: Stops,
    localTime: LocalTime
  ): JsDom.TypedTag[Div] = {
    val NextStop(location, content) = nextBusTime(stops, localTime)
    val dateFormat = DateTimeFormatter.ofPattern("h:mm a")
    TagsOnly.createBusTimeElement(
      location,
      content match {
        case Left(time) => Left(time.format(dateFormat))
        case Right(safeRideRecommendation) =>
          Right(TagsOnly.safeRideLink(safeRideRecommendation))
      }
    )
  }

}

case class Stops(location: StopLocation.Value, times: Seq[LocalTime])

case class SafeRideRecommendation(
  message: String,
  phoneNumber: String = "970-209-0519"
)

case class NextStop(location: StopLocation.Value,
                    content: Either[LocalTime, SafeRideRecommendation]
                    /* TODO: waitDuration: Duration*/ )

object TagsOnly {
  import scalatags.JsDom.all._

  val overallPageLayout =
    div(id := "container")(
      div(cls := "wrapper")(
        div(cls := "box c", id := "upcoming-buses")(
          h3(style := "text-align: center")(
            "Upcoming Buses"
          )
        ),
        div(cls := "box d")(
          div("Future Work: RTA buses")
        )
      )
    )

//  <a href="tel:123-456-7890">123-456-7890</a>
  def safeRideLink(
    safeRideRecommendation: SafeRideRecommendation
  ): JsDom.TypedTag[Anchor] =
    a(href := s"tel:${safeRideRecommendation.phoneNumber}")(
      safeRideRecommendation.message
    )

  def createBusTimeElement(
    location: StopLocation.Value,
    content: Either[String, JsDom.TypedTag[Anchor]]
    /* TODO: waitDuration: Duration*/
  ): JsDom.TypedTag[Div] =
    div(width:="100%", style := "text-align:right;" )(
      span(location.name),
      span(
        content match {
          case Left(contentString) => contentString
          case Right(phoneAnchor)  => phoneAnchor
        }
      )
    )

}

object DomManipulation {
  import scalatags.JsDom.all._

  val createAndApplyPageStructure: ZIO[Browser, Nothing, Node] =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          browser.dom
            .body()
            .appendChild(
              TagsOnly.overallPageLayout.render
            )
      )

  def appendMessageToPage(
    message: String
  ): ZIO[Browser, Throwable, Unit] =
    ZIO
      .environment[Browser]
      .map[Unit](
        browser =>
          browser.dom
            .body()
            .querySelector("#activity-log")
            .appendChild(div(message).render)
      )

  def addDivToUpcomingBusesSection(
    divToRender: JsDom.TypedTag[Div]
  ): ZIO[Browser, Nothing, Unit] =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          browser.dom
            .body()
            .querySelector("#upcoming-buses")
            .appendChild(divToRender.render)
      )
}

object Browser {

  trait Service {
    def body(): HTMLElement
  }
}

trait Browser {
  def dom: Browser.Service
}

trait BrowserLive extends Browser {

  def dom: Browser.Service =
    () => document.body
}
object BrowserLive extends BrowserLive
