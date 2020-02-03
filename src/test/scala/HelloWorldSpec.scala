import java.time.{LocalDate, LocalTime, OffsetDateTime, ZoneOffset}

import zio._
import zio.console._
import zio.test.{DefaultRunnableSpec, test, testM, _}
import zio.test.Assertion._
import zio.test.environment._
import HelloWorld._
import crestedbutte.BusTimes

object HelloWorld {
  def sayHello: ZIO[Console, Nothing, Unit] =
    console.putStrLn("Hello, World!")
}

object TimeHelpers {
  def simpleOffsetDateTime(localTime: String) =
    OffsetDateTime.of(
      LocalDate.parse("2010-05-03"),
      LocalTime.parse(localTime),
      ZoneOffset.ofHours(0))
}

object HelloWorldSpec
  extends DefaultRunnableSpec(
    suite("HelloWorldSpec")(
      testM("sayHello correctly displays output") {
        for {
          _      <- sayHello
          output <- TestConsole.output
        } yield assert(output, equalTo(Vector("Hello, World!\n")))
      },
      testM("find the next bus time") {
        val startTime = LocalTime.parse("07:10:00")
        val endTime = LocalTime.parse("23:40:00")
        val totalBusRunTime = java.time.Duration.between(startTime, endTime)
        val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds
        val stops =
            List.range(0, numberOfBusesPerDay)
              .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))
        for {
          _ <- TestClock.setDateTime(TimeHelpers.simpleOffsetDateTime("09:01:00"))
          result <- BusTimes.nextBusArrivalTime(stops)
        } yield assert(result.get, equalTo(LocalTime.parse("09:15:00")))
      },
      testM("really early morning check") {
        val startTime = LocalTime.parse("07:10:00")
        val endTime = LocalTime.parse("23:40:00")
        val totalBusRunTime = java.time.Duration.between(startTime, endTime)
        val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds
        val stops =
          List.range(0, numberOfBusesPerDay)
            .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))
        for {
          _ <- TestClock.setDateTime(TimeHelpers.simpleOffsetDateTime("05:00:00"))
          result <- BusTimes.nextBusArrivalTime(stops)
        } yield assert(result.get, equalTo(LocalTime.parse("07:00:00")))
      },
      testM("after last bus has run") {
        val startTime = LocalTime.parse("07:10:00")
        val endTime = LocalTime.parse("23:40:00")
        val totalBusRunTime = java.time.Duration.between(startTime, endTime)
        val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds
        val stops =
          List.range(0, numberOfBusesPerDay)
            .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))
        for {
          _ <- TestClock.setDateTime(TimeHelpers.simpleOffsetDateTime("23:00:00"))
          result <- BusTimes.nextBusArrivalTime(stops)
        } yield assert(result, equalTo(Option.empty))
      }
    )
  ) {

}