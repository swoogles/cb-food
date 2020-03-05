package crestedbutte

object ElementNames { // TODO Change to bus-specific name
  val BoxClass = "bill-box"

  object UrlManipulation {
    val rewriteUrl = "rewrite-url"
  }

  object TownShuttles {
    val containerName = "town-shuttles"
    val readableRouteName = "Town Loop"
  }

  val contentName = "upcoming-buses"
  val title = "upcoming-buses-title"

  object ThreeSeasonsLoop {
    val containerName = "three-seasons-loop"
    val readableRouteName = "Three Seasons Loop"
  }

  object RtaNorthbound {
    val containerName = "rta-northbound"
  }

  object CrystalCastleLoop {
    val containerName = "crystal-castle-loop"
  }

  object Notifications {
    val requestPermission = "request-notification-permission"
    val notificationAction = "notification-action"

    val submitMessageToServiceWorker =
      "submit-message-to-service-worker"
  }

}
