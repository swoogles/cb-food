package crestedbutte

import java.time.{DateTimeException, Instant, OffsetDateTime}
import java.util.concurrent.TimeUnit

import com.billding.time.{
  BusTime,
  ColoradoClock,
  FixedClock,
  TurboClock,
}
import crestedbutte.Browser.Browser
import crestedbutte.dom.{
  BulmaBehaviorLocal,
  DomManipulation,
  ServiceWorkerBillding,
}
import crestedbutte.routes._
import org.scalajs.dom.experimental.serviceworkers._
import org.scalajs.dom.raw.HTMLMediaElement
import zio.clock._
import zio.console.Console
import zio.duration.Duration
import zio.{App, Has, Schedule, ZIO, ZLayer}

import scala.util.{Failure, Success}

object MyApp extends App {
  HTMLMediaElement

  override def run(
    args: List[String],
  ): ZIO[zio.ZEnv, Nothing, zio.ExitCode] = {
    val myEnvironment =
      ZLayer.succeed(BrowserLive.browser) ++ Console.live ++
      ZLayer.succeed(ColoradoClock.live)

    fullApplicationLogic.provideLayer(myEnvironment).exitCode
  }

  def loopLogic(
    pageMode: AppMode.Value,
    restaurantGroups: Seq[RestaurantGroup],
  ): ZIO[Browser with Clock, DateTimeException, Unit] =
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

      _ <- updateUpcomingArrivalsOnPage(selectedRestaurantGroup,
                                        restaurantGroups)
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

  val fullApplicationLogic
    : ZIO[Clock with Has[Browser.Service] with Console,
          Nothing,
          Int] =
    for {
      browser    <- ZIO.access[Browser](_.get)
      console    <- ZIO.access[Console](_.get)
      clockParam <- ZIO.access[Clock](_.get)
      pageMode <- QueryParameters
        .getOptional("mode", AppMode.fromString)
        .map { _.getOrElse(AppMode.Production) }
      _ <- DomManipulation.createAndApplyPageStructure(
        TagsOnlyLocal
          .overallPageLayout(pageMode, restaurantGroups)
          .render,
      )
      _ <- UnsafeCallbacks.attachMenuBehavior
      fixedTime <- QueryParameters.getRequired("time",
                                               deserializeTimeString)
      clock = if (fixedTime.isDefined)
        ZLayer.succeed(
          TurboClock.TurboClock(
            s"2020-02-20T${fixedTime.get.toString}:00.00-07:00",
          ),
        )
      else ZLayer.succeed(clockParam)
      environmentDependencies = ZLayer.succeed(browser) ++ ZLayer
        .succeed(console) ++ clock
      _ <- ServiceWorkerBillding.register("./sw-opt.js")
      loopLogicInstantiated = loopLogic(pageMode, restaurantGroups)
        .provideLayer(
          // TODO Try to provide *only* a clock here.
          environmentDependencies,
        )
        .catchAll(failure => throw new RuntimeException("ack!")) // TODO Improve this shiz
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
      val restaurantsWithStatus =
        TimeCalculations.calculateCurrentRestaurantStatus(
          now,
          restaurantGroup,
        )
      DomManipulation.updateContentInsideElementAndReveal(
        restaurantGroup.componentName,
        TagsOnlyLocal
          .structuredSetOfUpcomingArrivals(
            restaurantsWithStatus,
            restaurantGroup.name,
          )
          .render,
        ElementNames.contentName,
      )
    } else {
      DomManipulation.hideElement(
        restaurantGroup.componentName,
      )
    }

  def updateUpcomingArrivalsOnPage(
    selectedRestaurantGroup: RestaurantGroup,
    restaurantGroups: Seq[RestaurantGroup],
  ): ZIO[Browser with Clock, Nothing, Unit] =
    for {
      clockProper <- ZIO.access[Clock](_.get)
      now <- clockProper.currentDateTime
        .catchAll(failure => throw new RuntimeException("ack!"))
      localTime = new BusTime(now.toLocalTime)
      modalIsOpen <- DomMonitoring.modalIsOpen
      _ <- if (modalIsOpen) ZIO.succeed()
      else
        ZIO.collectAll(
          restaurantGroups.map(
            updateCurrentRestaurantInfoInCity(
              _,
              selectedRestaurantGroup,
              now.toInstant,
            ),
          ),
        )
    } yield ()

}
