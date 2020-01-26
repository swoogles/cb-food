import java.io.IOException
import java.time.LocalTime
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
//      _ <- DomManipulation.addWelcomeMessage()
      now <- currentDateTime
      _ <- putStrLn("CurrentLocalTime: " + now.toLocalTime)
//      _ <- DomManipulation.addElementToPage(DomManipulation.createBusScheduleTable(BusTimes.mountaineerSquareBusStarts))

      _ <- BusTimes.printBusInfo
      nextBusAtMountaineer <- BusTimes.findNextBus(BusTimes.mountaineerSquareBusStarts)
      nextBusAtTeocalliDown <- BusTimes.findNextBus(BusTimes.teocalliDownMountainBusStarts)
      _ <- nextBusAtMountaineer match {
        case Some(nextBusTime) => DomManipulation.appendBusTime("Mountaineer Square: " + nextBusTime)
        case None => ZIO.succeed("No bus available. Time to call safe-ride!")
      }
      _ <- nextBusAtTeocalliDown match {
        case Some(nextBusTime) => DomManipulation.appendBusTime("Teocalli down mountain: " + nextBusTime)
        case None => ZIO.succeed("No bus available. Time to call safe-ride!")
      }
      _ <- ScheduleSandbox.liveBusses
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
  val mountaineerSquareBusStarts: Seq[LocalTime] =
    List.range(0, numberOfBusesPerDay)
      .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))

  val teocalliDownMountainBusStarts =
    mountaineerSquareBusStarts
    .map(_.plusMinutes(7))

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
}

object DomManipulation {
  import org.scalajs.dom
  import dom.document
  import scalatags.JsDom.all._

  val createPageStructure = ZIO {
    document.body.appendChild(
      div(id:="container")(
        div(id:="upcoming-buses", style:= "width:50%; float:left" )("Upcoming buses"),
        div(id:="activity-log", style := "margin-left:50%")("activity log"),
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

  def appendBusTime(message: String) =
    ZIO {
      val paragraph = div(message)
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
