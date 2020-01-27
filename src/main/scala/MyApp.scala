import java.io.IOException
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import org.scalajs.dom.html.Table
import scalatags.{JsDom, Text}
import zio.{App, Schedule, ZIO}
import zio.console.{putStrLn, _}
import zio.clock._
import zio._
import zio.duration.Duration

object MyApp extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    for {
      _ <- DomManipulation.createPageStructure
      _ <- DomManipulation.createGrid
      //      _ <- DomManipulation.addWelcomeMessage()
      now <- currentDateTime
      _ <- putStrLn("CurrentLocalTime: " + now.toLocalTime)
      //      _ <- DomManipulation.addElementToPage(DomManipulation.createBusScheduleTable(BusTimes.mountaineerSquareBusStarts))

      _ <- BusTimes.printBusInfo
      _ <- BusTimes.addNextBusTimeToPage(BusTimes.oldTownHallBusStarts)
      _ <- BusTimes.addNextBusTimeToPage(BusTimes.clarksBusStarts)
      _ <- BusTimes.addNextBusTimeToPage(BusTimes.fourWayUphillBusStarts)
//      _ <- ScheduleSandbox.liveBusses
    } yield {
      0

    }
  }
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
    } yield (
      timesAtStop
        .dropWhile(now.toLocalTime.isAfter(_))
      ).headOption

    def addNextBusTimeToPage(stops: Stops) =
      for {
        nextStop <- BusTimes.findNextBus(stops.times)
        _ <-
          nextStop match {
            case Some(nextBusTime) => DomManipulation.appendBusTime(stops.name, nextBusTime)
            case None => ZIO.succeed("No bus available. Time to call safe-ride!")
          }
      } yield {
      }

}

case class Stops( name: String, times: Seq[LocalTime])


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

  def appendBusTime(busStopName: String, time: LocalTime) =
    ZIO {
      val dateFormat = DateTimeFormatter.ofPattern("hh:mm a")
      val paragraph = div(style:="float:right; padding-right: 30px;")(s"$busStopName: " + time.format(dateFormat))
      document.body.querySelector("#upcoming-buses").appendChild(paragraph.render)
    }
      .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )

  def addWelcomeMessage() =
    ZIO {
      val paragraph = p("Let's Make a Bus Schedule App!")
      document.body.appendChild(paragraph.render)
    }
      .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )

  def addElementToPage(element: JsDom.TypedTag[Table]) = ZIO {
    document.body.appendChild(element.render)
  }
    .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )
}
