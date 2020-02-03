package crestedbutte

import zio.clock._
import zio.console.Console
import zio.{App, Schedule, ZIO}

object MyApp extends App {

  override def run(
    args: List[String]
  ): ZIO[zio.ZEnv, Nothing, Int] = {
    val myEnvironment
      : zio.clock.Clock with zio.console.Console with Browser =
      new Clock.Live with Console.Live with BrowserLive

    (for {
      _ <- DomManipulation.createAndApplyPageStructure
      _ <- addAllBusTimesToPage // This only executes once.
      /* Looping behavior for page:
      _   <- ZIO {  eventLoop } */
    } yield { 0 }).provide(myEnvironment)
  }

  val addAllBusTimesToPage
    : ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      localTime = now.toLocalTime
      upcomingArrivalAtAllStops = BusTimes.calculateUpcomingArrivalAtAllStops(localTime)
      _ <- DomManipulation.addDivToUpcomingBusesSection(
        TagsOnly.structuredSetOfUpcomingArrivals(upcomingArrivalAtAllStops)
      )
    } yield ()

}
