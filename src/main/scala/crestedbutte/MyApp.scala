package crestedbutte

import java.util.NoSuchElementException
import java.util.concurrent.TimeUnit

import crestedbutte.routes.{ThreeSeasonsTimes, TownShuttleTimes}
import org.scalajs.dom.Node
import zio.clock._
import zio.console.Console
import zio.console.putStrLn
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
      pageMode      <- getCurrentPageMode
      selectedRoute <- getRouteQueryParamValue
      _             <- putStrLn("SelectedRoute: " + selectedRoute)
      _ <- DomManipulation.createAndApplyPageStructure(
        pageMode
      ) // TODO Base on queryParam
      _ <- attachMenuBehavior
      _ <- registerServiceWorker()

      _ <- NotificationStuff.addNotificationPermissionRequestToButton
//      _ <- NotificationsStuff.addAlarmBehaviorToTimes
      _ <- NotificationStuff.displayNotificationPermission
      _ <- loopLogic(pageMode, selectedRoute)
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

  def loopLogic(pageMode: AppMode.Value, routeName: RouteName.Value) =
    for {
      _ <- updateUpcomingArrivalsOnPage(routeName)
      _ <- NotificationStuff.addAlarmBehaviorToTimes
      _ <- ModalBehavior.addModalOpenBehavior
      _ <- ModalBehavior.addModalCloseBehavior
      _ <- NotificationStuff.checkSubmittedAlarms
    } yield ()

  def updateUpcomingArrivalsForRoute(
    componentName: String,
    routeName: RouteName.Value,
    schedules: Seq[BusScheduleAtStop],
    routeMode: RouteMode.Value
  ) =
    for {
      upcomingArrivalAtAllTownShuttleStops <- TimeCalculations // SKIP IF NOT ACTIVE ROUTE
        .getUpComingArrivalsWithFullSchedule(
          Route(schedules, routeName)
        )
      _ <- DomManipulation.updateUpcomingBusSectionInsideElement(
        componentName,
        TagsOnly.structuredSetOfUpcomingArrivals(
          upcomingArrivalAtAllTownShuttleStops
        ),
        routeMode
      )
    } yield ()

  val modalIsOpen: ZIO[Browser, Nothing, Boolean] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .body()
          .querySelectorAll(".modal.is-active") // LONG SEARCH
          .length > 0
      }

  def updateUpcomingArrivalsOnPage(
    routeName: RouteName.Value
  ): ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      modalIsOpen <- modalIsOpen
      _ <- if (modalIsOpen) ZIO.succeed()
      else
        for {
          _ <- updateUpcomingArrivalsForRoute(
            ElementNames.TownShuttles.containerName,
            RouteName.TownLoop,
            TownShuttleTimes.townShuttleStops,
            if (routeName == RouteName.TownLoop) RouteMode.Active
            else RouteMode.Hidden
          )
          _ <- updateUpcomingArrivalsForRoute(
            ElementNames.ThreeSeasonsLoop.containerName,
            RouteName.ThreeSeasonsLoop,
            ThreeSeasonsTimes.allStops,
            if (routeName == RouteName.ThreeSeasonsLoop)
              RouteMode.Active
            else RouteMode.Hidden
          )
        } yield ()
    } yield ()

  val getRouteQueryParamValue =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          UrlParsing
            .getUrlParameter(
              browser.browser.window().location.toString,
              "route" // TODO Ugly string value
            )
            .flatMap(RouteName.fromString)
            .getOrElse(RouteName.TownLoop)
      )

  val getCurrentPageMode =
    ZIO.environment[Browser].map { browser =>
      UrlParsing // Make the url/query param functions part of the Browser.
        .getUrlParameter(
          browser.browser.window().location.toString,
          "mode" // TODO Ugly string value
        )
        .flatMap(rawString => AppMode.fromString(rawString))
        .getOrElse(AppMode.Production)
    }

  val attachMenuBehavior =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          browser.browser
            .window()
            .document
            .addEventListener(
              "DOMContentLoaded",
              (_: Any) =>
                browser.browser
                  .querySelectorAll(".navbar-burger")
                  .foreach(
                    node =>
                      node
                        .addEventListener(
                          "click",
                          menuCallbackBehavior(node, browser)
                        )
                  )
            )
      )

  def menuCallbackBehavior(node: Node, browser: Browser) =
    (_: MouseEvent) =>
      // Get the target from the "data-target" attribute
      // POTENTIALLY VERY EXPENSIVE. It's jumping back to the root of the document with this search.
      browser.browser
        .querySelector(
          "#" + node.attributes
            .getNamedItem("data-target")
            .value
        )
        .map(_.classList.toggle("is-active"))

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
                .querySelector(
                  "#" + ElementNames.Notifications.submitMessageToServiceWorker
                )
                .foreach(
                  _.addEventListener(
                    "click",
                    (_: MouseEvent) => {
                      println(
                        "submitting message to service worker"
                      )
                      registration.active.postMessage(
                        "Submitting a message to the serviceWorker!"
                      )
                    }
                  )
                )
              registration.update()
            case Failure(error) =>
              println(
                s"registerServiceWorker: service worker registration failed > ${error.printStackTrace()}"
              )
          }
      }
}
