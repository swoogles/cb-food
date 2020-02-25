package crestedbutte

import java.util.NoSuchElementException
import java.util.concurrent.TimeUnit

import crestedbutte.routes.{ThreeSeasonsTimes, TownShuttleTimes}
import zio.clock._
import zio.console.Console
import zio.duration.Duration
import zio.{App, Schedule, ZIO}
import org.scalajs.dom.experimental.serviceworkers._
import org.scalajs.dom.raw.{MouseEvent, NamedNodeMap, NodeList}

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
      pageMode <- getCurrentPageMode
      _ <- DomManipulation.createAndApplyPageStructure(
        pageMode
      ) // TODO Base on queryParam
      _ <- registerServiceWorker()
      _ <- NotificationStuff.addNotificationPermissionRequestToButton
//      _ <- NotificationsStuff.addAlarmBehaviorToTimes
      _ <- NotificationStuff.displayNotificationPermission
      _ <- loopLogic(pageMode)
        .provide(
          // TODO Try to provide *only* a clock here.
          if (pageMode == AppMode.Development)
            new LateNightClock.Fixed with Console.Live
            with BrowserLive
          else
            new Clock.Live with Console.Live with BrowserLive
        )
        // Currently, everytime I refresh, kills the modal
        .repeat(Schedule.spaced(Duration.apply(10, TimeUnit.SECONDS)))
    } yield {
      0
    }).provide(myEnvironment)
  }

  def loopLogic(pageMode: AppMode.Value) =
    for {
      _ <- updateUpcomingArrivalsOnPage
      _ <- NotificationStuff.addAlarmBehaviorToTimes
      _ <- ModalBehavior.addModalOpenBehavior
      _ <- ModalBehavior.addModalCloseBehavior
      _ <- NotificationStuff.checkSubmittedAlarms
    } yield ()

  def updateUpcomingArrivalsForRoute(
    componentName: String,
    readableRouteName: String,
    schedules: Seq[BusScheduleAtStop]
  ) =
    for {
      upcomingArrivalAtAllTownShuttleStops <- TimeCalculations
        .getUpComingArrivalsWithFullSchedule(
          Route(schedules, RouteName.TownLoop)
        )
      _ <- DomManipulation.updateUpcomingBusSectionInsideElement(
        componentName,
        TagsOnly.structuredSetOfUpcomingArrivals(
          upcomingArrivalAtAllTownShuttleStops
        )
      )
    } yield ()

  val modalIsOpen: ZIO[Browser, Nothing, Boolean] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .body()
          .querySelectorAll(".modal.is-active")
          .length > 0
      }

  val updateUpcomingArrivalsOnPage
    : ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      modalIsOpen <- modalIsOpen
      _ <- if (modalIsOpen) ZIO.succeed()
      else
        for {
          _ <- updateUpcomingArrivalsForRoute(
            ElementNames.TownShuttles.containerName,
            ElementNames.TownShuttles.readableRouteName,
            TownShuttleTimes.townShuttleStops
          )
          upcomingArrivalAtCondoloopStops <- TimeCalculations
            .getUpComingArrivalsWithFullSchedule(
              Route(ThreeSeasonsTimes.allStops,
                    RouteName.ThreeSeasonsLoop)
            )
          _ <- DomManipulation.updateUpcomingBusSectionInsideElement(
            ElementNames.ThreeSeasonsLoop.containerName,
            TagsOnly.structuredSetOfUpcomingArrivals(
              upcomingArrivalAtCondoloopStops
            )
          )
        } yield ()
    } yield ()

  val getCurrentPageMode =
    ZIO.environment[Browser].map { browser =>
      UrlParsing
        .getUrlParameter(
          browser.browser.window().location.toString,
          "mode" // TODO Ugly string value
        )
        .flatMap(rawString => AppMode.fromString(rawString))
        .getOrElse(AppMode.Production)
    }

  def registerServiceWorker() =
    ZIO
      .environment[Browser]
      .map { browser =>
        toServiceWorkerNavigator(browser.browser.window().navigator).serviceWorker
          .register("./sw-opt.js")
          .toFuture
          .onComplete {
            case Success(registration) =>
              browser.browser
                .body()
                .querySelector(
                  "#" + ElementNames.Notifications.submitMessageToServiceWorker
                )
                .addEventListener(
                  "click",
                  (mouseEvent: MouseEvent) => {
                    println("submitting message to service worker")
                    registration.active.postMessage(
                      "Submitting a message to the serviceWorker!"
                    )
                  }
                )
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
