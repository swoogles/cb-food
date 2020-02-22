package crestedbutte

import java.util.concurrent.TimeUnit

import crestedbutte.time.BusTime
import org.scalajs.dom
import zio.clock._
import zio.console.Console
import zio.console.putStrLn
import zio.stream.Stream
import zio.duration.Duration
import zio.{App, Schedule, ZIO}
import org.scalajs.dom._
import org.scalajs.dom.experimental.{
  Notification,
  NotificationOptions
}
import org.scalajs.dom.experimental.serviceworkers._

import scala.collection.mutable
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Promise
import scala.util.{Failure, Success}
// TODO Ew. Try to get this removed after first version of PWA is working
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.{Event, EventTarget, MessageEvent, MessagePort}
import org.scalajs.jquery._

@js.native
trait BootstrapJQuery extends JQuery {
  def modal(action: String): BootstrapJQuery = js.native
  def modal(options: js.Any): BootstrapJQuery = js.native
  def foundation(): Unit = js.native
}

object MyApp extends App {

  implicit def jq2bootstrap(jq: JQuery): BootstrapJQuery =
    jq.asInstanceOf[BootstrapJQuery]
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
//      _ <- ZIO.succeed { jQuery("").foundation()  js.Dynamic.global.d }
//      _ <- ZIO.succeed { jQuery(dom.document).foundation() }
      _ <- registerServiceWorker()
      _ <- NotificationsStuff.addNotificationPermissionRequestToButton
//      _ <- NotificationsStuff.addAlarmBehaviorToTimes
      _ <- NotificationsStuff.displayNotificationPermission
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
          NotificationsStuff.addAlarmBehaviorToTimes
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
        .getUpComingArrivals(
          Route(TownShuttleTimes.townShuttleStops)
        )
      _ <- DomManipulation.updateUpcomingBusesSection(
        TagsOnly.structuredSetOfUpcomingArrivals(
          upcomingArrivalAtAllStops,
          TownShuttleTimes.townShuttleStops
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

  object NotificationsStuff {

//    var $status = document.getElementById("status")

    val addNotificationPermissionRequestToButton
      : ZIO[Browser, Nothing, Unit] =
      ZIO.environment[Browser].map { browser =>
        val requestPermissionButton =
          browser.browser
            .body()
            .querySelector(
              s"#${ElementNames.Notifications.requestPermission}"
            )
        if (requestPermissionButton != null)
          requestPermissionButton.addEventListener(
            "click",
            (event: Any) =>
              if (Notification.permission == "default")
                Notification.requestPermission(
                  response =>
                    println(
                      "Notification requestPermission response: " + response
                    )
                )
              else if (Notification.permission == "denied")
                println(
                  "They denied permission to notifications. Give it up."
                )
              else if (Notification.permission == "granted")
                println("we already have permission.")
              else
                println(
                  "Uknown permission state: " + Notification.permission
                )
          )
      }

    val addAlarmBehaviorToTimes = ZIO.environment[Browser].map {
      browser =>
        if (Notification.permission == "granted") {
          val actionButton =
            browser.browser
              .body()
              .querySelectorAll(
                s".arrival-time"
              )
          println("Selected arrival-time elements")
          if (actionButton != null)
            for (i <- 0 to actionButton.length) {
              val item = actionButton.item(i)
              if (item != null)
                item
                  .addEventListener(
                    "click",
                    (event: MouseEvent) => {
                      println(
                        "lossless value: " + event.target
                          .asInstanceOf[org.scalajs.dom.raw.Element]
                          .getAttribute("data-lossless-value")
                      )
                      desiredAlarms
                        .appendAll(
                          Seq(
                            BusTime(
                              event.target
                                .asInstanceOf[
                                  org.scalajs.dom.raw.Element
                                ]
                                .getAttribute("data-lossless-value")
                                .replace("'", "")
                                .trim
                            )
                          )
                        )

                      /*
                dom.window.setTimeout(
                  () =>
                    new Notification("The bus is coming!",
                                     NotificationOptions(
                                       vibrate = js.Array(100d)
                                     )),
                  10000
                )

                     */
                    }
                  )
            }
        }
    }

    val displayNotificationPermission = ZIO.environment[Browser].map {
      browser =>
        val actionButton =
          browser.browser
            .body()
            .querySelector(
              s"#${ElementNames.Notifications.notificationAction}"
            )
        if (actionButton != null)
          actionButton
            .addEventListener(
              "click",
              (event: MouseEvent) => {
                println(
                  "event.relatedTarget: " + event.target
                )
                desiredAlarms
                  .appendAll(
                    Seq(
                      BusTime(
                        event.target
                          .asInstanceOf[org.scalajs.dom.raw.Element]
                          .innerHTML // TODO ewwwww
                      )
                    )
                  )

                /*
                dom.window.setTimeout(
                  () =>
                    new Notification("The bus is coming!",
                                     NotificationOptions(
                                       vibrate = js.Array(100d)
                                     )),
                  10000
                )

               */
              }
            )
    }
    /*
    if ("Notification" in window) {
      $status.innerText = Notification.permission;
    }

    function requestPermission() {
      if (!('Notification' in window)) {
        alert('Notification API not supported!');
        return;
      }

      Notification.requestPermission(function (result) {
        $status.innerText = result;
      });
    }

   */
  }

//  def messageStuff(): Unit =
//    toServiceWorkerNavigator(browser.dom.window().navigator).serviceWorker

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
