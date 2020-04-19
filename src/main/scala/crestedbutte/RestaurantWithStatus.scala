package crestedbutte

case class RestaurantWithStatus(
  restaurantWithSchedule: RestaurantWithSchedule,
  carryOutStatus: RestaurantStatus,
  deliveryStatus: RestaurantStatus,
)
