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


/*

 */
object StopLocation extends Enumeration {
  type StopLocation = Value
  val
    OldTownHall,
    Clarks,
    FourWayUphill,
    TeocalliUphill,
    MountaineerSquare,
    TeocalliDownhill,
    FourwayDownhill
      = Value
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
  val endTime = LocalTime.parse("23:00:00")
  val totalBusRunTime = java.time.Duration.between(startTime, endTime)
  val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds
  val oldTownHallBusStarts: Stops =
    Stops("Old Town Hall",
    List.range(0, numberOfBusesPerDay)
      .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))
    )

  val clarksBusStarts =
    Stops("6th/Belleview (Clarks)",
      oldTownHallBusStarts.times
        .map(_.plusMinutes(4))
    )

  val fourWayUphillBusStarts =
    Stops("4-way (Uphill)",
    clarksBusStarts.times
      .map(_.plusMinutes(1))
    )

  val teocalliUphillBusStarts =
    Stops("Teocalli (Uphill)",
      fourWayUphillBusStarts.times
        .map(_.plusMinutes(1))
    )


  val mountaineerSquareBusStarts =
    Stops("Mountaineer Square",
      teocalliUphillBusStarts.times
        .map(_.plusMinutes(9))
    )

  val teocalliDownhillBusStarts =
    Stops("Teocalli (Downhill)",
      mountaineerSquareBusStarts.times
        .map(_.plusMinutes(6)) // TODO Confirm time
    )

  val fourwayDownhill =
    Stops("4-way (Downhill)",
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
            val typedNextStop = NextStop(stops.name, Some(nextBusTime))
            DomManipulation.createBusTimeElement(typedNextStop)
          }
          case None => {
            val typedNextStop = NextStop(stops.name, Option.empty)
            DomManipulation.createBusTimeElement(typedNextStop)
          }
        }
      }

  val addAllBusTimesToPage: ZIO[Browser with Clock with Console, Nothing, Unit] = {
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
      _ <- DomManipulation.addDivToUpcomingBusesSection(timedElements)
    } yield ()
  }

}

case class Stops( name: String, times: Seq[LocalTime])
case class NextStop( name: String, time: Option[LocalTime])


object DomManipulation {
  import org.scalajs.dom
  import dom.document
  import scalatags.JsDom.all._

  val createGrid = ZIO {
    document.body.querySelector("#container").appendChild(
    div(cls:="wrapper")(
      div(cls:="box a")("A"),
      div(cls:="box b")("B"),
      div(cls:="box c", id:="upcoming-buses")("Upcoming Buses"),
      div(cls:="box d")(
        div(cls:="box e")("E"),
        div(cls:="box f")("F"),
        div(cls:="box g")("G"),
    )
    ).render
    )
  }
    .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )

  val createPageStructure = ZIO {
    document.body.appendChild(
      div(id:="container")(
//        div(id:="upcoming-buses", style:= "width:50%; float:left" )("Upcoming buses"),
      ).render
    )
  }
    .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )

  def createBusScheduleTable(times: Seq[LocalTime]): JsDom.TypedTag[Table] = {
    table(
      times
        .map(time => tr(td(time.toString)))
    )
  }

  def appendMessageToPage(message: String) =
    ZIO {
      println("Are we actually adding the new messages?")
      val paragraph = div(message)
      document.body.querySelector("#activity-log").appendChild(paragraph.render)
    }
      .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )

  def createBusTimeElement(nextStop: NextStop) = {
    val dateFormat = DateTimeFormatter.ofPattern("h:mm a")
    val finalTimeOutput = nextStop.time match {
      case Some(time) => time.format(dateFormat)
      case None => "Time to call saferide!"
    }
    div(style:="float:right; padding-right: 30px;")(s"${nextStop.name}: " + finalTimeOutput)

  }

  def appendBusTime(nextStop: NextStop) =
    for {
      browser <- ZIO.environment[Browser]
    } yield {
      browser.dom.body().querySelector("#upcoming-buses").appendChild(createBusTimeElement(nextStop).render)
    }

  def addElementToPage(element: JsDom.TypedTag[Table]) =
    for {
      browser <- ZIO.environment[Browser]
  } yield {
      browser.dom.body().appendChild(element.render)
      }

  def addDivToUpcomingBusesSection(elements: Seq[JsDom.TypedTag[Div]]) =
    for {
      browser <- ZIO.environment[Browser]
    } yield {
      elements.foreach(element => browser.dom.body().querySelector("#upcoming-buses").appendChild(element.render))
    }
//    ZIO {
//
//    "done"
//  }
//    .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )
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
