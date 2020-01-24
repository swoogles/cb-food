import java.io.IOException
import java.time.LocalTime

import zio.{App, Schedule, ZIO}
import zio.console._
import zio.clock._
import zio._
import zio.duration.Duration

object MyApp extends App {
  def numberOfBussesPerDay(spacing: Duration, start: LocalTime, end: LocalTime) = ???

  val driveFromMountaineerToTeocalli =
    Schedule.once && Schedule.spaced(duration.durationInt(2).seconds)

  def realBusSchedule(numberOfBussesPerDay: Int): Schedule[Clock, Any, (Int, Int)] =
    Schedule.recurs(numberOfBussesPerDay) &&
      Schedule.spaced(duration.durationInt(2).seconds)

  val liveBusses: ZIO[Clock with Console, Nothing, (Int, Int)] =

    putStrLn("Bus is leaving Mountaineer Square!")
      .flatMap(_ => ZIO.sleep(duration.durationInt(2).seconds))
      .flatMap(_ => putStrLn("Bus arrived at teocalli!"))
      .repeat(realBusSchedule(5))

  // TODO ScalaJS version of App
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    val repetitions = Schedule.recurs(3) && Schedule.spaced(duration.durationInt(1).second)
    val logic: ZIO[Console with Clock, IOException, Int] = for {
      _ <- putStr("name: ")
      name <- ZIO.succeed("Hardcoded Harry")
      now <- currentDateTime
      _ <- putStrLn(s"hello, $name - $now")
    } yield (0)
    logic
      .repeat(repetitions)

    .fold(
      error => 1,
      success => 0)

    liveBusses
      .flatMap{case (left, right)=> putStrLn("left: " + left + " right: " + right)}
      .map( _ => 0)
  }
}
