package crestedbutte

import java.util.concurrent.TimeUnit

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

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Promise
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
      _ <- NotificationsStuff.addNotificationPermissionRequestToButton
      _ <- NotificationsStuff.displayNotificationPermission
      _ <- updateUpcomingArrivalsOnPage
        .repeat(Schedule.spaced(Duration.apply(20, TimeUnit.SECONDS)))
    } yield {
      0
    }).provide(myEnvironment)
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
        .flatMap(rawString => PageMode.fromString(rawString))
        .getOrElse(PageMode.Production)
    }

  object NotificationsStuff {

//    var $status = document.getElementById("status")

    val addNotificationPermissionRequestToButton =
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
              (event: Any) =>
                dom.window.setTimeout(
                  () =>
                    new Notification("The bus is coming!",
                                     NotificationOptions(
                                       vibrate = js.Array(100d)
                                     )),
                  10000
                )
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

  def registerServiceWorker() =
    ZIO
      .environment[Browser]
      .map { browser =>
        toServiceWorkerNavigator(browser.dom.window().navigator).serviceWorker
          .register("./sw-opt.js")
          .toFuture
          .onComplete {
            case Success(registration) =>
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
