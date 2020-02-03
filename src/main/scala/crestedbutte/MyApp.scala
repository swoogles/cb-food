package crestedbutte

import zio.clock._
import zio.console.Console
import zio.{App, ZIO}

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
    : ZIO[Browser with Clock with Console, Nothing, Unit] = {
    import scalatags.JsDom.all._ // TODO WHAT AN OBVIOUS WART!!!
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      localTime = now.toLocalTime

      busTimes = BusTimes.townShuttleStops
      busTimeElemnts = busTimes.map(BusTimes.createNextBusTimeElement(_, localTime))
      _ <- DomManipulation.addDivToUpcomingBusesSection(
        div(
          busTimeElemnts
        )
      )
    } yield ()
  }

}
