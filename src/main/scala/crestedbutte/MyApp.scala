package crestedbutte

import java.time.{Instant, OffsetDateTime}
import java.util.concurrent.TimeUnit

import crestedbutte.dom.BulmaBehaviorLocal
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
      new ColoradoClock.Live with Console.Live with BrowserLive

    fullApplicationLogic.provide(myEnvironment)
  }

  def loopLogic(
    pageMode: AppMode.Value,
    components: Seq[ComponentData],
  ): ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      serviceAreaOpt <- QueryParameters.getOptional(
        "route",
        x => Some(x),
      )
      selectedComponent: ComponentData = serviceAreaOpt
        .flatMap(
          serviceAreaParam =>
            components.find(
              _.restaurantGroup.restaurantGroupName
                .elementNameMatches(serviceAreaParam),
            ),
        )
        .getOrElse(components.head)

      _ <- ZIO.succeed(
        println(
          "selectedComponent: " + selectedComponent.restaurantGroup.restaurantGroupName.humanFriendlyName,
        ),
      )
      now <- TimeCalculations.now
      _ <- updateUpcomingArrivalsOnPage(selectedComponent,
                                        components,
                                        now)
      _ <- NotificationStuff.addAlarmBehaviorToTimes
      _ <- ModalBehavior.addModalOpenBehavior
      _ <- ModalBehavior.addModalCloseBehavior
      _ <- UnsafeCallbacks.attachCardClickBehavior
      _ <- NotificationStuff.checkSubmittedAlarms
    } yield ()

  private val components: Seq[ComponentData] =
    Seq(
      CbRestaurantsAndSchedules,
    ).map(ComponentData) ++: Seq(
      ComponentData(
        GunnisonRestaurants,
      ),
    )

  def deserializeTimeString(rawTime: String): OffsetDateTime =
    OffsetDateTime.parse(
      s"2020-02-20T${rawTime}:00.00-07:00",
    )

  val fullApplicationLogic: ZIO[Clock with Browser, Nothing, Int] =
    for {
      pageMode <- QueryParameters
        .getOptional("mode", AppMode.fromString)
        .map { paramAttempt =>
          paramAttempt
            .getOrElse(AppMode.Production)
        }
      _ <- DomManipulation.createAndApplyPageStructure(
        pageMode,
        components,
      )
      _ <- UnsafeCallbacks.attachMenuBehavior
      fixedTime <- QueryParameters.getRequired("time",
                                               deserializeTimeString)
      environmentDependencies = if (fixedTime.isDefined) { // TODO use map instead
        println("generic queryParam currentTime: " + fixedTime.get)
        new FixedClock.Fixed(
          fixedTime.get.toString,
        ) with Console.Live with BrowserLive
      } else
        new ColoradoClock.Live with Console.Live with BrowserLive
      _ <- registerServiceWorker()
      _ <- NotificationStuff.addNotificationPermissionRequestToButton
      _ <- NotificationStuff.displayNotificationPermission
      _ <- BulmaBehaviorLocal.addMenuBehavior(
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
        .repeat(
          Schedule.spaced(Duration.apply(600, TimeUnit.SECONDS)),
        )
    } yield {
      0
    }

  def updateUpcomingArrivalsForRoute(
    componentData: ComponentData,
    currentlySelectedRoute: ComponentData,
    now: Instant,
  ) =
    if (componentData == currentlySelectedRoute) {
      println(
        "SelectedRoute: " + currentlySelectedRoute.componentName,
      )
      val restaurantsWithStatus =
        TimeCalculations.calculateUpcomingArrivalAtAllStops(
          now,
          componentData.restaurantGroup,
        )
      for {
        _ <- DomManipulation.updateUpcomingBusSectionInsideElement(
          componentData.componentName,
          TagsOnlyLocal.structuredSetOfUpcomingArrivals(
            restaurantsWithStatus,
            componentData.restaurantGroup.restaurantGroupName,
          ),
        )
      } yield ()
    } else {
      println("Hiding other element: " + componentData.componentName)
      DomManipulation.hideUpcomingBusSectionInsideElement(
        componentData.componentName,
      )
    }

  def updateUpcomingArrivalsOnPage(
    selectedRoute: ComponentData,
    components: Seq[ComponentData],
    now: Instant,
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
              now: Instant,
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
