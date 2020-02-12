package crestedbutte

import java.util.concurrent.TimeUnit

import zio.clock._
import zio.console.Console
import zio.console.putStrLn
import zio.duration.Duration
import zio.{App, Schedule, ZIO}
import org.scalajs.dom._
import org.scalajs.dom.experimental.serviceworkers._

import scala.util.{Failure, Success}
// TODO Ew. Try to get this removed after first version of PWA is working
import scala.concurrent.ExecutionContext.Implicits.global

object MyApp extends App {

  override def run(
    args: List[String]
  ): ZIO[zio.ZEnv, Nothing, Int] = {
    val myEnvironment
      : zio.clock.Clock with zio.console.Console with Browser =
      new Clock.Live with Console.Live with BrowserLive
    registerServiceWorker()

    (for {
      _ <- DomManipulation.createAndApplyPageStructure
      _ <- registerServiceWorker()
      _ <- updateUpcomingArrivalsOnPage
        .repeat(Schedule.spaced(Duration.apply(20, TimeUnit.SECONDS)))
    } yield {
      0
    }).provide(myEnvironment)
  }

  val updateUpcomingArrivalsOnPage
    : ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      upcomingArrivalAtAllStops <- TownShuttleTimes.getUpComingArrivals
      _ <- DomManipulation.updateUpcomingBusesSection(
        TagsOnly.structuredSetOfUpcomingArrivals(
          upcomingArrivalAtAllStops
        )
      )
    } yield ()

  def registerServiceWorker() =
    ZIO
      .environment[Browser]
      .map { browser =>
        toServiceWorkerNavigator(browser.dom.window().navigator).serviceWorker
          .register("./sw-opt.js")
          .toFuture
          .onComplete {
            case Success(registration) =>
              println(
                "registerServiceWorker: registered service worker in a monad properly accesing the env"
              )
              registration.update()
            case Failure(error) =>
              println(
                s"registerServiceWorker: service worker registration failed > ${error.printStackTrace()}"
              )
          }
      }
}
