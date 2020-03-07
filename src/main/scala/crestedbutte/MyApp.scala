package crestedbutte

import java.util.concurrent.TimeUnit

import crestedbutte.dom.BulmaBehavior
import crestedbutte.routes._
import org.scalajs.dom.experimental.serviceworkers._
import org.scalajs.dom.raw.MouseEvent
import zio.clock._
import zio.console.Console
import zio.duration.Duration
import zio.{App, Schedule, ZIO}

import scala.util.{Failure, Success}

object MyApp extends App {

  override def run(
    args: List[String],
  ): ZIO[zio.ZEnv, Nothing, Int] = {
    val myEnvironment =
      new Clock.Live with Console.Live with BrowserLive

    fullApplicationLogic.provide(myEnvironment)
  }

  def loopLogic(
    pageMode: AppMode.Value,
    components: Seq[ComponentData],
  ): ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      routeNameOpt <- QueryParameters.getRouteQueryParamValue
      selectedComponent: ComponentData = routeNameOpt
        .flatMap(
          routeNameStringParam =>
            components.find(
              _.namedRoute.routeName
                .elementNameMatches(routeNameStringParam),
            ),
        )
        .getOrElse(components.head)

      _ <- updateUpcomingArrivalsOnPage(selectedComponent, components)
      _ <- NotificationStuff.addAlarmBehaviorToTimes
      _ <- ModalBehavior.addModalOpenBehavior
      _ <- ModalBehavior.addModalCloseBehavior
      _ <- NotificationStuff.checkSubmittedAlarms
    } yield ()

  val mtnExpressRoutes =
    new CompanyRoutes("Mtn Express",
                      Seq(
                        TownShuttleTimes,
                        CrystalCastleShuttle,
                        ColumbineLoop,
                        SnodgrassShuttle,
                        ThreeSeasonsTimes,
                      ))

  private val components: Seq[ComponentData] =
    mtnExpressRoutes.routesWithTimes
      .map(ComponentData) ++:
    Seq(
      ComponentData(
        RtaNorthbound.fullSchedule,
      ),
      ComponentData(
        RtaSouthbound.fullSchedule,
      ),
    )

  val fullApplicationLogic =
    for {
      pageMode  <- QueryParameters.getCurrentPageMode
      fixedTime <- QueryParameters.getCurrentTimeParamValue
      _ <- DomManipulation.createAndApplyPageStructure(
        pageMode,
        components,
      )
      _ <- UnsafeCallbacks.attachMenuBehavior
      environmentDependencies = if (fixedTime.isDefined)
        new FixedClock.Fixed(
          s"2020-02-20T${fixedTime.get.toString}:00.00-07:00",
        ) with Console.Live with BrowserLive
      else
        new Clock.Live with Console.Live with BrowserLive
      _ <- UnsafeCallbacks.attachUrlRewriteBehavior(
        pageMode,
        environmentDependencies,
        components,
      )
      _ <- registerServiceWorker()
      _ <- NotificationStuff.addNotificationPermissionRequestToButton
      _ <- NotificationStuff.displayNotificationPermission
      _ <- BulmaBehavior.addMenuBehavior(
        loopLogic(pageMode, components)
          .provide(
            // TODO Try to provide *only* a clock here.
            environmentDependencies,
          ),
      )
      _ <- loopLogic(pageMode, components)
        .provide(
          // TODO Try to provide *only* a clock here.
          environmentDependencies,
        )
        .repeat(Schedule.spaced(Duration.apply(10, TimeUnit.SECONDS)))
    } yield {
      0
    }

  def updateUpcomingArrivalsForRoute(
    componentData: ComponentData,
    currentlySelectedRoute: ComponentData,
  ) =
    if (componentData == currentlySelectedRoute)
      for {
        arrivalsAtAllRouteStops <- TimeCalculations // SKIP IF NOT ACTIVE ROUTE
          .getUpComingArrivalsWithFullSchedule(
            componentData.namedRoute,
          )
        _ <- DomManipulation.updateUpcomingBusSectionInsideElement(
          componentData.componentName,
          TagsOnly.structuredSetOfUpcomingArrivals(
            arrivalsAtAllRouteStops,
          ),
        )
      } yield ()
    else
      DomManipulation.hideUpcomingBusSectionInsideElement(
        componentData.componentName,
      )

  def updateUpcomingArrivalsOnPage(
    selectedRoute: ComponentData,
    components: Seq[ComponentData],
  ): ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      modalIsOpen <- DomMonitoring.modalIsOpen
      _ <- if (modalIsOpen) ZIO.succeed()
      else
        ZIO.sequence(
          components.map(
            updateUpcomingArrivalsForRoute(
              _,
              selectedRoute,
            ),
          ),
        )
    } yield ()

  def registerServiceWorker() =
    ZIO
      .environment[Browser]
      .map { browser =>
        // TODO Ew. Try to get this removed after first version of PWA is working
        import scala.concurrent.ExecutionContext.Implicits.global

        val serviceWorker =
          toServiceWorkerNavigator(browser.browser.window().navigator).serviceWorker
            .register("./sw-opt.js")
            .toFuture
            .onComplete {
              case Success(registration) =>
                browser.browser
                  .querySelector(
                    "#" + ElementNames.Notifications.submitMessageToServiceWorker,
                  )
                  .foreach(
                    _.addEventListener(
                      "click",
                      (_: MouseEvent) => {
                        println(
                          "submitting message to service worker",
                        )
                        registration.active.postMessage(
                          "Submitting a message to the serviceWorker!",
                        )
                      },
                    ),
                  )
                registration.update()
              case Failure(error) =>
                println(
                  s"registerServiceWorker: service worker registration failed > ${error.printStackTrace()}",
                )
            }
      }
}
