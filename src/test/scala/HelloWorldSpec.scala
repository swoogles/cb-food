import zio._
import zio.console._
import zio.test.{DefaultRunnableSpec, test, testM, _}
import zio.test.Assertion._
import zio.test.environment._
import HelloWorld._
import crestedbutte.BusTimeCalculations

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
      test("find the next bus time") {
        val startTime = BusTime("07:10")
        val endTime = BusTime("23:40")
        val totalBusRunTime = startTime.between(endTime)
        val numberOfBusesPerDay = totalBusRunTime.dividedBy(BusDuration.ofMinutes(15))
        val stops =
            List.range(0, numberOfBusesPerDay)
              .map(index => startTime.plusMinutes(15 * index.toInt))
          val result = BusTimeCalculations.nextBusArrivalTime(stops, BusTime("09:01:00"))
        assert(result.get, equalTo(BusTime("09:10:00")))
      },
      test("really early morning check") {
        val startTime = BusTime("07:10")
        val endTime = BusTime("23:40")
        val totalBusRunTime = startTime.between( endTime)
        val numberOfBusesPerDay = totalBusRunTime.dividedBy(BusDuration.ofMinutes(15))
        val stops =
          List.range(0, numberOfBusesPerDay)
            .map(index => startTime.plusMinutes(15 * index.toInt))
          val result = BusTimeCalculations.nextBusArrivalTime(stops, BusTime("05:00"))
        assert(result.get, equalTo(BusTime("07:10")))
      },
      test("after last bus has run") {
        val startTime = BusTime("07:10")
        val endTime = BusTime("23:40")
        val totalBusRunTime = startTime.between( endTime)
        val numberOfBusesPerDay = totalBusRunTime.dividedBy(BusDuration.ofMinutes(15))
        val stops =
          List.range(0, numberOfBusesPerDay)
            .map(index => startTime.plusMinutes(15 * index.toInt))
          val result = BusTimeCalculations.nextBusArrivalTime(stops, BusTime("23:50"))
        assert(result, equalTo(Option.empty))
      },
        test("bus is arriving this minute") {
        val startTime = BusTime("07:10:00")
        val endTime = BusTime("23:40:00")
        val totalBusRunTime = startTime.between(endTime)
          val numberOfBusesPerDay = totalBusRunTime.dividedBy(BusDuration.ofMinutes(15))
        val stops =
          List.range(0, numberOfBusesPerDay)
            .map(index => startTime.plusMinutes(15 * index.toInt))
          val localTime = BusTime("23:10:02")
        val result = BusTimeCalculations.nextBusArrivalTime(stops, localTime)
        assert(result.get, equalTo(BusTime("23:10")))
      }
    )
  ) {

}