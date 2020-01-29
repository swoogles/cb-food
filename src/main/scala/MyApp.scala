import java.io.IOException
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import org.scalajs.dom.document
import org.scalajs.dom.html.{Div, Table}
import org.scalajs.dom.raw.HTMLElement
import scalatags.{JsDom, Text}
import zio.{App, Schedule, ZIO}
import zio.console.{putStrLn, _}
import zio.clock._
import zio._
import zio.duration.Duration

object MyApp extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    val myEnvironment: zio.clock.Clock with zio.console.Console with Browser =
      new Clock.Live with Console.Live with BrowserLive


    (for {
      _ <- DomManipulation.createPageStructure
      _ <- DomManipulation.createGrid
      now <- currentDateTime
      _ <- putStrLn("CurrentLocalTime: " + now.toLocalTime)
      //      _ <- DomManipulation.addElementToPage(DomManipulation.createBusScheduleTable(BusTimes.mountaineerSquareBusStarts))

      _ <- BusTimes.printBusInfo
      _ <- BusTimes.addAllBusTimesToPage
//      _ <- ScheduleSandbox.liveBusses
    } yield {
      0

    }).provide(myEnvironment)
  }
}

object StopLocation extends Enumeration {
  protected case class Val(name: String) extends super.Val(name)
  import scala.language.implicitConversions
  implicit def valueToStopLocationVal(x: Value) = x.asInstanceOf[Val]

  type StopLocation = Value

  val OldTownHall = Val("Old Town Hall")
  val Clarks = Val("6th/Belleview (Clarks)")
  val FourWayUphill = Val("4-way (Uphill)")
  val TeocalliUphill = Val("Teocalli (Uphill)")
  val MountaineerSquare = Val("Mountaineer Square")
  val TeocalliDownhill = Val("Teocalli (Downhill)")
  val FourwayDownhill = Val("4-way (Downhill)")
}

object BusTimes {
  /*
  Regular Winter Schedule: November 27th through April 12th

    Every 15 Minutes from 7:10 AM to Midnight
                                        Stop	Times	            First Bus	  Last Bus
    Old Town Hall (2nd and Elk)	        :10, :25, :40, :55	    7:10 AM	    11:40 PM
    6th and Belleview	                  :59, :14, :29, :44	    7:14 AM	    11:44 PM
    4-Way Stop	                        :00, :15, :30, :45 	    7:15 AM	    11:45 PM
    Teocalli	                          :01, :16, :31, :46	    7:16 AM	    11:46 PM
    Mountaineer Square/Transit Center	  :00, :15, :30, :45	    7:30 AM 	  12:00 AM
   */

  val startTime = LocalTime.parse("07:00:00")
  val endTime = LocalTime.parse("23:40:00")
  val totalBusRunTime = java.time.Duration.between(startTime, endTime)
  val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds
  val oldTownHallBusStarts: Stops =
    Stops(StopLocation.OldTownHall,
    List.range(0, numberOfBusesPerDay)
      .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))
    )

  val clarksBusStarts =
    Stops( StopLocation.Clarks,

      oldTownHallBusStarts.times
        .map(_.plusMinutes(4))
    )

  val fourWayUphillBusStarts =
    Stops(StopLocation.FourWayUphill,
    clarksBusStarts.times
      .map(_.plusMinutes(1))
    )

  val teocalliUphillBusStarts =
    Stops(StopLocation.TeocalliUphill,
      fourWayUphillBusStarts.times
        .map(_.plusMinutes(1))
    )


  val mountaineerSquareBusStarts =
    Stops(StopLocation.MountaineerSquare,
      teocalliUphillBusStarts.times
        .map(_.plusMinutes(9))
    )

  val teocalliDownhillBusStarts =
    Stops(StopLocation.TeocalliDownhill,
      mountaineerSquareBusStarts.times
        .map(_.plusMinutes(6)) // TODO Confirm time
    )

  val fourwayDownhill =
    Stops(StopLocation.FourwayDownhill,
      teocalliDownhillBusStarts.times
        .map(_.plusMinutes(1)) // TODO Confirm time
    )


  val printBusInfo =
    for {
      _ <- putStrLn("First Bus: " +  BusTimes.startTime)
      _ <- putStrLn("Last Bus: " +  BusTimes.endTime)
      _ <- putStrLn("Total Bus Run Time:" +  BusTimes.totalBusRunTime)
      _ <- putStrLn("Number of buses per day:" +  BusTimes.numberOfBusesPerDay)
    } yield ()

  def findNextBus(timesAtStop: Seq[LocalTime]): ZIO[Clock with Console, Nothing, Option[LocalTime]] =
    for {
      clockProper <- ZIO.environment[Clock]
      now <-  clockProper.clock.currentDateTime
      _ <- ZIO.succeed { println("findNextBus Now: " + now.toLocalTime) }
    } yield
      timesAtStop
        .dropWhile(now.toLocalTime.isAfter(_))
      .headOption

    def createNextBusTimeElement(stops: Stops) =
      for {
        nextStop <- BusTimes.findNextBus(stops.times)
      } yield {
        nextStop match {
          case Some(nextBusTime) => {
            val typedNextStop = NextStop(stops.location, Some(nextBusTime)) // Update here.
            DomManipulation.createBusTimeElement(typedNextStop)
          }
          case None => {
            val typedNextStop = NextStop(stops.location, Option.empty)
            DomManipulation.createBusTimeElement(typedNextStop)
          }
        }
      }

  val addAllBusTimesToPage: ZIO[Browser with Clock with Console, Nothing, Unit] = {
    import scalatags.JsDom.all._
    val timedBusStopElements: ZIO[Clock with Console, Nothing, List[JsDom.TypedTag[Div]]] = ZIO.collectAll(
      List(
        createNextBusTimeElement(BusTimes.oldTownHallBusStarts),
        createNextBusTimeElement(BusTimes.clarksBusStarts),
        createNextBusTimeElement(BusTimes.fourWayUphillBusStarts),
        createNextBusTimeElement(BusTimes.teocalliUphillBusStarts),
        createNextBusTimeElement(BusTimes.mountaineerSquareBusStarts),
        createNextBusTimeElement(BusTimes.teocalliDownhillBusStarts),
        createNextBusTimeElement(BusTimes.fourwayDownhill),
      )
    )
    for {
      timedElements <- timedBusStopElements
      _ <- DomManipulation.addDivToUpcomingBusesSection(div(timedElements))
    } yield ()
  }

}

case class Stops(location: StopLocation.Value, times: Seq[LocalTime])
case class NextStop(location: StopLocation.Value, time: Option[LocalTime])


object DomManipulation {
  import org.scalajs.dom
  import dom.document
  import scalatags.JsDom.all._

  val createGrid =

    for {
      browser <- ZIO.environment[Browser]
    } yield
      browser.dom.body.querySelector("#container").appendChild(
        // TODO Create this HTML elsewhere
        div(cls:="wrapper")(
          div(cls:="box c", id:="upcoming-buses")(h3(style:="text-align: center")("Upcoming Buses")),
          div(cls:="box d")(
            div("Future Work: RTA buses"),
          )
        ).render
      )

  val createPageStructure =
    for {
      browser <- ZIO.environment[Browser]
    } yield
      browser.dom.body.appendChild(
        div(id:="container")(
        ).render
      )

  def createBusScheduleTable(times: Seq[LocalTime]): JsDom.TypedTag[Table] = {
    table(
      times
        .map(time => tr(td(time.toString)))
    )
  }

  def appendMessageToPage(message: String) =
    for {
      browser <- ZIO.environment[Browser]
      _ <- ZIO { document.body.querySelector("#activity-log").appendChild(div(message).render) }
    } yield ()


  def createBusTimeElement(nextStop: NextStop) = {
    val dateFormat = DateTimeFormatter.ofPattern("h:mm a")
    val finalTimeOutput = nextStop.time match {
      case Some(time) => time.format(dateFormat)
      case None => "Time to call saferide!"
    }
    div(style:="float:right;")(
      s"${nextStop.location.name}: " + finalTimeOutput)

  }

  def addElementToPage(element: JsDom.TypedTag[Table]) =
    for {
      browser <- ZIO.environment[Browser]
    } yield
        browser.dom.body()
          .appendChild(element.render)

  def addDivToUpcomingBusesSection(divToRender: JsDom.TypedTag[Div]) =
    for {
      browser <- ZIO.environment[Browser]
      _ <- ZIO { browser.dom.body()
        .querySelector("#upcoming-buses")
        .appendChild(divToRender.render)
      }.catchAll(_ => ZIO.succeed("Ignoring failed dom operations"))
    } yield ()
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
    new Browser.Service {
      def body(): HTMLElement = document.body
    }
}
object BrowserLive extends BrowserLive
