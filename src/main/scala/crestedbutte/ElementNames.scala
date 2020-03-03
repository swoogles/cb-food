package crestedbutte

object ElementNames {
  val BoxClass = "bill-box"

  object UrlManipulation {
    val rewriteUrl = "rewrite-url"
  }

  object TownShuttles {
    val containerName = "town-shuttles"
    val contentName = "upcoming-buses"
    val title = "upcoming-buses-title"
    val readableRouteName = "Town Loop"
  }

  object ThreeSeasonsLoop {
    val containerName = "three-seasons-loop"
    val contentName = "upcoming-buses"
    val title = "upcoming-buses-title"
    val readableRouteName = "Three Seasons Loop"
  }

  object RtaNorthbound {
    val containerName = "rta-northbound"
    val contentName = "upcoming-buses"
//    val title = "upcoming-buses-title"
//    val readableRouteName = "Three Seasons Loop"
  }

  object Notifications {
    val requestPermission = "request-notification-permission"
    val notificationAction = "notification-action"

    val submitMessageToServiceWorker =
      "submit-message-to-service-worker"
  }

}
