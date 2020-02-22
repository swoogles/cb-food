package crestedbutte

import crestedbutte.time.BusTime
import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.experimental.{
  Notification,
  NotificationOptions
}
import zio.ZIO
import zio.clock.Clock

import scala.collection.mutable
import scala.scalajs.js

object NotificationStuff {

  val desiredAlarms = mutable.Queue.empty[BusTime]
  desiredAlarms.empty

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
      println("Notification.permission: " + Notification.permission)
      if (Notification.permission == "granted") {
        val actionButton =
          browser.browser
            .body()
            .querySelectorAll(
              ".arrival-time"
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
                  }
                )
          }
      }
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

            }
          )
  }
}
