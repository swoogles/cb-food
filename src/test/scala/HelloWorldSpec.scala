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
      test("find the next bus time") {
        val startTime = LocalTime.parse("07:10:00")
        val endTime = LocalTime.parse("23:40:00")
        val totalBusRunTime = java.time.Duration.between(startTime, endTime)
        val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds
        val stops =
            List.range(0, numberOfBusesPerDay)
              .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))
          val result = BusTimes.nextBusArrivalTime(stops, LocalTime.parse("09:01:00"))
        assert(result.get, equalTo(LocalTime.parse("09:10:00")))
      },
      test("really early morning check") {
        val startTime = LocalTime.parse("07:10:00")
        val endTime = LocalTime.parse("23:40:00")
        val totalBusRunTime = java.time.Duration.between(startTime, endTime)
        val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds
        val stops =
          List.range(0, numberOfBusesPerDay)
            .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))
          val result = BusTimes.nextBusArrivalTime(stops, LocalTime.parse("05:00:00"))
        assert(result.get, equalTo(LocalTime.parse("07:10:00")))
      },
      test("after last bus has run") {
        val startTime = LocalTime.parse("07:10:00")
        val endTime = LocalTime.parse("23:40:00")
        val totalBusRunTime = java.time.Duration.between(startTime, endTime)
        val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration.ofMinutes(15).getSeconds
        val stops =
          List.range(0, numberOfBusesPerDay)
            .map(index => startTime.plus(java.time.Duration.ofMinutes(15).multipliedBy(index)))
          val result = BusTimes.nextBusArrivalTime(stops, LocalTime.parse("23:50:00"))
        assert(result, equalTo(Option.empty))
      }
    )
  ) {

}