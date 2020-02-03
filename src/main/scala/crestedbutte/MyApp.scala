package crestedbutte

import java.util.concurrent.TimeUnit

import zio.clock._
import zio.console.Console
import zio.console.putStrLn
import zio.duration.Duration
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
      _ <- addAllBusTimesToPage.flatMap(_=> putStrLn("meaningful repetition")) .repeat(Schedule.spaced(Duration.apply(20, TimeUnit.SECONDS))) // This only executes once.
      /* Looping behavior for page:
      _   <- ZIO {  eventLoop } */
    } yield { 0 }).provide(myEnvironment)
  }

  val getUpComingArrivals =
    for {
      clockProper <- ZIO.environment[Clock]
      now <- clockProper.clock.currentDateTime
      localTime = now.toLocalTime
    } yield { BusTimes.calculateUpcomingArrivalAtAllStops(localTime)}


  val addAllBusTimesToPage
    : ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      upcomingArrivalAtAllStops <- getUpComingArrivals
      _ <- DomManipulation.updateUpcomingBusesSection(
        TagsOnly.structuredSetOfUpcomingArrivals(
          upcomingArrivalAtAllStops
        )
        // Currently, this only repeats "updating" the dom with the same value for upcomingArrivalAtAllStops
      )
    } yield ()

}
