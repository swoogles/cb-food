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
//      _ <- NotificationsStuff.addNotificationPermissionRequestToButton
//      _ <- NotificationsStuff.addAlarmBehaviorToTimes
      _ <- NotificationsStuff.displayNotificationPermission
      _ <- updateUpcomingArrivalsOnPage
        .flatMap { _ =>
          NotificationsStuff.addAlarmBehaviorToTimes
        }
        .flatMap { _ =>
          checkSubmittedAlarms
        }
        .repeat(Schedule.spaced(Duration.apply(5, TimeUnit.SECONDS)))
    } yield {
      0
    }).provide(myEnvironment)
  }

  val checkSubmittedAlarms: ZIO[Browser, Nothing, Unit] =
    ZIO.environment[Browser].map { browser =>
      println("Checking for submitted alarms")
      val busTimes = desiredAlarms.dequeueAll { _ =>
        true
      }
      busTimes.map { busTime =>
        new Notification(s"The bus is coming at ${busTime.toString}!",
                         NotificationOptions(
                           vibrate = js.Array(100d)
                         ))
      }
      ()
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

  val getCurrentPageMode =
    ZIO.environment[Browser].map { browser =>
      UrlParsing
        .getUrlParameter(
          browser.dom.window().location.toString,
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
          browser.dom
            .body()
            .querySelector(
              s"#${ElementNames.Notifications.requestPermission}"
            )
        if (requestPermissionButton != null)
          requestPermissionButton.addEventListener(
            "click",
            (event: Any) =>
              Notification.requestPermission(
                response =>
                  println(
                    "Notification requestPermission response: " + response
                  )
              )
          )
      }

    val addAlarmBehaviorToTimes = ZIO.environment[Browser].map {
      browser =>
        val actionButton =
          browser.dom
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
                      "event.relatedTarget: " + event.target
                    )
                    println(
                      "InnerHtml: " + event.target
                        .asInstanceOf[org.scalajs.dom.raw.Element]
                        .innerHTML
                    )
                    desiredAlarms
                      .appendAll(
                        Seq(
                          BusTime(
                            event.target
                              .asInstanceOf[
                                org.scalajs.dom.raw.Element
                              ]
                              .innerHTML // TODO ewwwww
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

    val displayNotificationPermission = ZIO.environment[Browser].map {
      browser =>
        val actionButton =
          browser.dom
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
        toServiceWorkerNavigator(browser.dom.window().navigator).serviceWorker
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
