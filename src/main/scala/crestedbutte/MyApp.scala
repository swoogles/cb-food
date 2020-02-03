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
      _ <- updateUpcomingArrivalsOnPage
        .repeat(Schedule.spaced(Duration.apply(20, TimeUnit.SECONDS)))
    } yield { 0 }).provide(myEnvironment)
  }

  val updateUpcomingArrivalsOnPage
    : ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      upcomingArrivalAtAllStops <- BusTimes.getUpComingArrivals
      _ <- DomManipulation.updateUpcomingBusesSection(
        TagsOnly.structuredSetOfUpcomingArrivals(
          upcomingArrivalAtAllStops
        )
      )
    } yield ()

}
