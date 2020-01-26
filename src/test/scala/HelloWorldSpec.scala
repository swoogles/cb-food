import java.time.{LocalDate, LocalTime, OffsetDateTime, ZoneOffset}

import zio._
import zio.console._
import zio.test.{DefaultRunnableSpec, test, testM, _}
import zio.test.Assertion._
import zio.test.environment._
import HelloWorld._
import zio.duration.Duration

object HelloWorld {
  def sayHello: ZIO[Console, Nothing, Unit] =
    console.putStrLn("Hello, World!")
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
        for {
          _ <- TestClock.setDateTime(OffsetDateTime.of(
            LocalDate.parse("2010-05-03"),
            LocalTime.parse("09:01:00"),
          ZoneOffset.ofHours(0)))
//          _ <- TestClock.setTime(Duration.Zero)
//          clock: TestClock.Test <- environment.TestClock.makeTest(TestClock.DefaultData)
          result <- BusTimes.findNextBus
//          _      <- clock.live.provide(BusTimes.findNextBus)

//          output <- TestConsole.output
        } yield assert(result, equalTo(Vector("Hello, World!\n")))
      }
    )
  )