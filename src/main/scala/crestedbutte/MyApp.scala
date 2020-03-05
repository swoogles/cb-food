package crestedbutte

import java.util.concurrent.TimeUnit

import crestedbutte.dom.BulmaBehavior
import crestedbutte.routes.{
  RtaNorthbound,
  ThreeSeasonsTimes,
  TownShuttleTimes
}
import crestedbutte.time.BusTime
import org.scalajs.dom.Node
import zio.clock._
import zio.console.Console
import zio.console.putStrLn
import zio.duration.Duration
import zio.{App, DefaultRuntime, Schedule, ZIO}
import org.scalajs.dom.experimental.serviceworkers._
import org.scalajs.dom.raw.{MouseEvent, NamedNodeMap, NodeList}
import zio.scheduler.SchedulerLive

import scala.util.{Failure, Success, Try}
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
      pageMode  <- QueryParams.getCurrentPageMode
      fixedTime <- QueryParams.getCurrentTimeParamValue
      _ <- DomManipulation.createAndApplyPageStructure(
        pageMode
      )
      _ <- attachMenuBehavior
      environmentDependencies: SchedulerLive with Clock with Console.Live with BrowserLive = if (fixedTime.isDefined)
        new FixedClock.Fixed(
          s"2020-02-20T${fixedTime.get.toString}:00.00-07:00"
        ) with Console.Live with BrowserLive
      else
        new Clock.Live with Console.Live with BrowserLive
      _ <- attachUrlRewriteBehavior(pageMode, environmentDependencies)
      _ <- registerServiceWorker()

      _ <- NotificationStuff.addNotificationPermissionRequestToButton
      _ <- NotificationStuff.displayNotificationPermission
      _ <- BulmaBehavior.addMenuBehavior(
        loopLogic(pageMode)
          .provide(
            // TODO Try to provide *only* a clock here.
            environmentDependencies
          )
      )
      _ <- loopLogic(pageMode)
        .provide(
          // TODO Try to provide *only* a clock here.
          environmentDependencies
        )
        // Currently, everytime I refresh, kills the modal
        .repeat(Schedule.spaced(Duration.apply(10, TimeUnit.SECONDS)))
    } yield {
      0
    }).provide(myEnvironment)
  }

  def loopLogic(
    pageMode: AppMode.Value
  ): ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      routeName <- QueryParams.getRouteQueryParamValue
      _         <- updateUpcomingArrivalsOnPage(routeName)
      _         <- NotificationStuff.addAlarmBehaviorToTimes
      _         <- ModalBehavior.addModalOpenBehavior
      _         <- ModalBehavior.addModalCloseBehavior
      _         <- NotificationStuff.checkSubmittedAlarms
    } yield ()

  def updateUpcomingArrivalsForRoute(
    componentData: ComponentData
  ) =
    if (componentData.routeName == componentData.currentlySelectedRoute)
      for {
        upcomingArrivalAtAllTownShuttleStops <- TimeCalculations // SKIP IF NOT ACTIVE ROUTE
          .getUpComingArrivalsWithFullSchedule(
            Route(componentData.schedules, componentData.routeName)
          )
        _ <- DomManipulation.updateUpcomingBusSectionInsideElement(
          componentData.componentName,
          TagsOnly.structuredSetOfUpcomingArrivals(
            upcomingArrivalAtAllTownShuttleStops
          )
        )
      } yield ()
    else {
      DomManipulation.hideUpcomingBusSectionInsideElement(
        componentData.componentName
      )
    }

  val modalIsOpen: ZIO[Browser, Nothing, Boolean] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .body()
          .querySelectorAll(".modal.is-active") // LONG SEARCH
          .length > 0
      }

  case class ComponentData(
    componentName: String,
    routeName: RouteName.Value,
    schedules: Seq[BusScheduleAtStop],
    currentlySelectedRoute: RouteName.Value
  )

  def updateUpcomingArrivalsOnPage(
    selectedRoute: RouteName.Value
  ): ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      modalIsOpen <- modalIsOpen
      _ <- if (modalIsOpen) ZIO.succeed()
      else
        for {
          _ <- updateUpcomingArrivalsForRoute(
            ComponentData(
              ElementNames.TownShuttles.containerName,
              RouteName.TownLoop,
              TownShuttleTimes.townShuttleStops,
              selectedRoute
            )
          )
          _ <- updateUpcomingArrivalsForRoute(
            ComponentData(ElementNames.RtaNorthbound.containerName,
                          RouteName.RtaNorthbound,
                          RtaNorthbound.stops,
                          selectedRoute)
          )
          _ <- updateUpcomingArrivalsForRoute(
            ComponentData(ElementNames.ThreeSeasonsLoop.containerName,
                          RouteName.ThreeSeasonsLoop,
                          ThreeSeasonsTimes.allStops,
                          selectedRoute)
          )
        } yield ()
    } yield ()

  object QueryParams {

    val getCurrentTimeParamValue =
      ZIO
        .environment[Browser]
        .map(
          browser =>
            UrlParsing
              .getUrlParameter(
                browser.browser.window().location.toString,
                "time" // TODO Ugly string value
              )
              .map(BusTime(_))
        )

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
  }

  def attachUrlRewriteBehavior(
    pageMode: AppMode.Value,
    environmentDependencies: SchedulerLive
      with Clock
      with Console.Live
      with BrowserLive
  ) =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .querySelector(
            ModalBehavior.id(ElementNames.UrlManipulation.rewriteUrl)
          ) // TODO Find better spot for .id function
          .foreach(
            _.addEventListener(
              "click",
              (_: MouseEvent) => {
//                if (browser.browser.url()) // Get current route query param, toggle route
                browser.browser
                  .rewriteCurrentUrl("route", "Three_Seasons_Loop")
                new DefaultRuntime {}.unsafeRun(
                  loopLogic(pageMode).provide(environmentDependencies)
                )
              }
            )
          )
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
        val serviceWorker =
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
