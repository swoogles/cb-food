package crestedbutte

import crestedbutte.MyApp.desiredAlarms // TODO Uh oh. Super bad here.
import crestedbutte.time.BusTime
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.experimental.Notification
import zio.ZIO

object NotificationStuff {

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

            }
          )
  }
}
