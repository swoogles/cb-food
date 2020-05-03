package crestedbutte

import java.time.{Instant, OffsetDateTime}
import java.util.concurrent.TimeUnit

import com.billding.time.{ColoradoClock, FixedClock}
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
    restaurantGroups: Seq[RestaurantGroup],
  ): ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      serviceAreaOpt <- QueryParameters.getOptional(
        "route",
        x => Some(x),
      )
      selectedRestaurantGroup: RestaurantGroup = serviceAreaOpt
        .flatMap(
          serviceAreaParam =>
            restaurantGroups.find(
              _.name
                .elementNameMatches(serviceAreaParam),
            ),
        )
        .getOrElse(restaurantGroups.head)

      _ <- ZIO.succeed(
        println(
          "selectedRestaurantGroup: " + selectedRestaurantGroup.name.humanFriendlyName,
        ),
      )
      now <- TimeCalculations.now
      _ <- updateUpcomingArrivalsOnPage(selectedRestaurantGroup,
                                        restaurantGroups,
                                        now)
      _ <- ModalBehavior.addModalOpenBehavior
      _ <- ModalBehavior.addModalCloseBehavior
      _ <- UnsafeCallbacks.attachCardClickBehavior
    } yield ()

  private val restaurantGroups: Seq[RestaurantGroup] =
    Seq(
      CbRestaurantsAndSchedules,
      GunnisonRestaurants,
    )

  def deserializeTimeString(rawTime: String): OffsetDateTime =
    OffsetDateTime.parse(
      s"2020-02-20T${rawTime}:00.00-07:00",
    )

  val fullApplicationLogic: ZIO[Clock with Browser, Nothing, Int] =
    for {
      pageMode <- QueryParameters
        .getOptional("mode", AppMode.fromString)
        .map { _.getOrElse(AppMode.Production) }
      _ <- DomManipulation.createAndApplyPageStructure(
        pageMode,
        restaurantGroups,
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
      loopLogicInstantiated = loopLogic(pageMode, restaurantGroups)
        .provide(
          // TODO Try to provide *only* a clock here.
          environmentDependencies,
        )
      _ <- BulmaBehaviorLocal.addMenuBehavior(
        loopLogicInstantiated,
      )
      _ <- loopLogicInstantiated
        .repeat(
          Schedule.spaced(Duration.apply(600, TimeUnit.SECONDS)),
        )
    } yield {
      0
    }

  def updateCurrentRestaurantInfoInCity(
    restaurantGroup: RestaurantGroup,
    currentlySelectedRestaurantGroup: RestaurantGroup,
    now: Instant,
  ) =
    if (restaurantGroup == currentlySelectedRestaurantGroup) {
      println(
        "SelectedRoute: " + currentlySelectedRestaurantGroup.componentName,
      )
      val restaurantsWithStatus =
        TimeCalculations.calculateCurrentRestaurantStatus(
          now,
          restaurantGroup,
        )
      for {
        _ <- DomManipulation.updateRestaurantSectionInsideElement(
          restaurantGroup.componentName,
          TagsOnlyLocal.structuredSetOfUpcomingArrivals(
            restaurantsWithStatus,
            restaurantGroup.name,
          ),
        )
      } yield ()
    } else {
      println(
        "Hiding other element: " + restaurantGroup.componentName,
      )
      DomManipulation.hideElement(
        restaurantGroup.componentName,
      )
    }

  def updateUpcomingArrivalsOnPage(
    selectedRestaurantGroup: RestaurantGroup,
    restaurantGroups: Seq[RestaurantGroup],
    now: Instant,
  ): ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      modalIsOpen <- DomMonitoring.modalIsOpen
      _ <- if (modalIsOpen) ZIO.succeed()
      else
        ZIO.sequence(
          restaurantGroups.map(
            updateCurrentRestaurantInfoInCity(
              _,
              selectedRestaurantGroup,
              now: Instant,
            ),
          ),
        )
    } yield ()

  def registerServiceWorker() =
    ZIO
      .environment[Browser]
      .map {
        registerServiceWorkerLogic
      }

  def registerServiceWorkerLogic(browser: Browser) = {
    // TODO Ew. Try to get this removed after first version of PWA is working
    import scala.concurrent.ExecutionContext.Implicits.global

    toServiceWorkerNavigator(browser.browser.window().navigator).serviceWorker
      .register("./sw-opt.js")
      .toFuture
      .onComplete {
        case Success(registration) =>
          registration.update()
        case Failure(error) =>
          println(
            s"registerServiceWorker: service worker registration failed > ${error.printStackTrace()}",
          )
      }
  }

}
