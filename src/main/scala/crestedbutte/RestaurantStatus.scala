package crestedbutte
sealed trait RestaurantStatus {}

object Closed extends RestaurantStatus
object OpeningSoon extends RestaurantStatus
object Open extends RestaurantStatus
object ClosingSoon extends RestaurantStatus
object Unknown extends RestaurantStatus
