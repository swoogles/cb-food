import zio._
import zio.console._
import zio.test.{DefaultRunnableSpec, test, testM, _}
import zio.test.Assertion._
import zio.test.environment._
import crestedbutte.BusTimeCalculations
import crestedbutte.time.{BusDuration, BusTime}
import crestedbutte.time.BusDuration.toBusDuration // Enables Int.minutes syntax

object BusTimeCalculationsSpec
    extends DefaultRunnableSpec(
      suite("HelloWorldSpec")(
        testM("sayHello correctly displays output") {
          for {
            _      <- putStrLn("Hello, World!")
            output <- TestConsole.output
          } yield assert(output, equalTo(Vector("Hello, World!\n")))
        },
        test(
          "find the next bus time while in the middle of the scehdule"
        ) {
          assert(
            BusTimeCalculations.nextBusArrivalTime(
              List(BusTime("09:00"), BusTime("09:15")), // TODO This callsite (and whole test class) screams for a proper BusSchedule type
              BusTime("09:10")
            ),
            isSome(equalTo(BusTime("09:15")))
          )
        },
        test("really early morning check") {
          assert(
            BusTimeCalculations.nextBusArrivalTime(
              List(BusTime("07:10")), // TODO This callsite (and whole test class) screams for a proper BusSchedule type
              BusTime("05:00")
            ),
            isSome(equalTo(BusTime("07:10")))
          )
        },
        test("after last bus has run") {
          assert(BusTimeCalculations.nextBusArrivalTime(
                   List(BusTime("23:40")),
                   BusTime("23:50")
                 ),
                 equalTo(Option.empty))
        },
        test("bus is arriving this minute") {
          assert(
            BusTimeCalculations.nextBusArrivalTime(
              List(BusTime("23:40")),
              BusTime("23:40")
            ),
            isSome(equalTo(BusTime("23:40")))
          )
        }
      )
    ) {}
