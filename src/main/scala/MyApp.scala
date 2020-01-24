import java.io.IOException
import java.time.LocalTime
import java.util.concurrent.TimeUnit

import zio.{App, Schedule, ZIO}
import zio.console.{putStrLn, _}
import zio.clock._
import zio._
import zio.duration.Duration

object MyApp extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    val logic: ZIO[Console with Clock, IOException, Int] = for {
      _ <- putStr("name: ")
      name <- ZIO.succeed("Hardcoded Harry")
      now <- currentDateTime
      _ <- putStrLn(s"hello, $name - $now")
    } yield (0)

    for {
      _ <- DomManipulation.addWelcomeMessage()
      now <- currentDateTime
      _ <- putStrLn("CurrentDateTime: " + now)
      nowTime <- currentTime(TimeUnit.MINUTES)
      _ <- putStrLn("CurrentTime in minutes: " + nowTime)

      _ <- BusTimes.printBusInfo
      _ <- ScheduleSandbox.liveBusses
//        (left, right) <- putStrLn("left: " + left + " right: " + right)
    } yield {
      0

    }
  }
}

object ScheduleSandbox {

  def numberOfBussesPerDay(spacing: Duration, start: LocalTime, end: LocalTime) = ???

  val driveFromMountaineerToTeocalli =
    Schedule.once && Schedule.spaced(duration.durationInt(2).seconds)

  def realBusSchedule(numberOfBussesPerDay: Int): Schedule[Clock, Any, (Int, Int)] =
    Schedule.recurs(numberOfBussesPerDay) &&
      Schedule.spaced(duration.durationInt(2).seconds)

  val singleBusRoute =
    DomManipulation.appendMessageToPage("Bus is leaving Mountaineer Square!")
      .flatMap(_ => ZIO.sleep(duration.durationInt(2).seconds))
      .flatMap(_ => DomManipulation.appendMessageToPage("Bus arrived at teocalli!"))

  val liveBusses =
    singleBusRoute
      .repeat(realBusSchedule(5))
}

object BusTimes {
  val startTime = LocalTime.parse("07:00:00")
  val endTime = LocalTime.parse("23:00:00")
  val totalBusRunTime = java.time.Duration.between(startTime, endTime)
  val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds

  val printBusInfo =
    for {
      _ <- putStrLn("First Bus: " +  BusTimes.startTime)
      _ <- putStrLn("Last Bus: " +  BusTimes.endTime)
      _ <- putStrLn("Total Bus Run Time:" +  BusTimes.totalBusRunTime)
      _ <- putStrLn("Number of buses per day:" +  BusTimes.numberOfBusesPerDay)
    } yield ()
}

object DomManipulation {
  import org.scalajs.dom
  import dom.document

  def appendMessageToPage(message: String) =
    for {
      _ <- putStrLn(message)
    } yield
    ZIO {
    val textNode = document.createTextNode(message)
    val paragraph = document.createElement("div")
    paragraph.appendChild(textNode)
    document.body.appendChild(paragraph)
  }
    .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )

  def addWelcomeMessage() = ZIO {
    val textNode = document.createTextNode("Let's Make a Bus Schedule App!")
    val paragraph = document.createElement("p")
      .appendChild(textNode)
    document.body.appendChild(paragraph)
  }
    .catchAll( error => ZIO.succeed("Guess we don't care about failed dom manipulation") )
}
