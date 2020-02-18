package crestedbutte

import zio._
import zio.console._
import zio.test.{DefaultRunnableSpec, test, testM, _}
import zio.test.Assertion._
import zio.test.environment._
import crestedbutte.BusTimeCalculations
import crestedbutte.time.{BusDuration, BusTime}
import crestedbutte.time.BusDuration.toBusDuration // Enables Int.minutes syntax

object BusTimeSpec
  extends DefaultRunnableSpec(
    suite("HelloWorldSpec")(
      test("bus is catchable") {
        val now = BusTime("07:10")
        val busTime = BusTime("07:15")
        assert(BusTime.catchableBus(now, busTime), equalTo(true))
      },
      test("bus is catchable even if it's PM") {
        val now = BusTime("11:55")
        val busTime = BusTime("12:05")
        assert(BusTime.catchableBus(now, busTime), equalTo(true))
      },

      test("duration between times is accurate") {
        val now = BusTime("11:55")
        val busTime = BusTime("12:05")
        println("Minutes between: " + now.between(busTime).toMinutes)
        assert(now.between(busTime), equalTo(10.minutes))
      },
      suite("parsing")(
        test("parses morning time") {
          assert(
          BusTime.parseIdeal("02:05").toString, equalTo("02:05"))
        }
      )

    )
  ) { }
