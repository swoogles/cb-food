package crestedbutte

import java.util.concurrent.TimeUnit

import crestedbutte.time.BusTime
import org.scalajs.dom
import zio.clock._
import zio.console.Console
import zio.duration.Duration
import zio.{App, Schedule, ZIO}
import org.scalajs.dom._
import org.scalajs.dom.experimental.{
  Notification,
  NotificationOptions
}
import org.scalajs.dom.experimental.serviceworkers._

import scala.collection.mutable
import scala.scalajs.js
import scala.util.{Failure, Success}
// TODO Ew. Try to get this removed after first version of PWA is working
import scala.concurrent.ExecutionContext.Implicits.global

object MyApp extends App {

  val desiredAlarms = mutable.Queue.empty[BusTime]
  desiredAlarms.empty

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
      _ <- updateUpcomingArrivalsOnPage
        .provide(
          // TODO Try to provide *only* a clock here.
          if (pageMode == AppMode.Development)
            new LateNightClock.Fixed
            with Console.Live with BrowserLive
          else
            new Clock.Live with Console.Live with BrowserLive
        )
        .flatMap { _ =>
          NotificationStuff.addAlarmBehaviorToTimes
        }
        .flatMap { _ =>
          checkSubmittedAlarms
        }
        // Currently, everytime I refresh, kills the modal
        .repeat(Schedule.spaced(Duration.apply(30, TimeUnit.SECONDS)))
    } yield {
      0
    }).provide(myEnvironment)
  }

  val checkSubmittedAlarms: ZIO[Clock, Nothing, Unit] =
    for {
      clock <- ZIO.environment[Clock]
      now   <- clock.clock.currentDateTime
      localTime = new BusTime(now.toLocalTime)
    } yield {
      println("Checking for submitted alarms")
      // TODO Make sure it's at least 2 minutes in the future (or whatever offset is appropriate)
      val busTimes = desiredAlarms.dequeueAll { _ =>
        true
      }
      println("Now: " + localTime)
      busTimes.map { busTime =>
        println("bustime: " + busTime)
        println(
          "Minutes until arrival: " + localTime
            .between(busTime)
            .toMinutes
        )
        val headsUpAmount = 3 // minutes
        if (localTime
              .between(busTime)
              .toMinutes >= headsUpAmount)
          dom.window.setTimeout(
            () =>
              // Read submitted time, find difference between it and the current time, then submit a setInterval function
              // with the appropriate delay
              new Notification(
                s"The ${busTime.toString} bus is arriving in ${headsUpAmount} minutes!",
                NotificationOptions(
                  vibrate = js.Array(100d)
                )
              ),
            (localTime
              .between(busTime)
              .toMinutes - headsUpAmount) * 60 * 1000
          )
      }
      ()
    }

  val updateUpcomingArrivalsOnPage
    : ZIO[Browser with Clock with Console, Nothing, Unit] =
    for {
      upcomingArrivalAtAllStops <- TimeCalculations
        .getUpComingArrivalsWithFullSchedule(
          Route(TownShuttleTimes.townShuttleStops)
        )
      _ <- DomManipulation.updateUpcomingBusesSection(
        TagsOnly.structuredSetOfUpcomingArrivals(
          upcomingArrivalAtAllStops
        )
      )
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
              registration.active.postMessage(
                "Submitting a message to the serviceWorker!"
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
