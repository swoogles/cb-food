package crestedbutte

case class RestaurantWithStatus(
  restaurantWithSchedule: Restaurant,
  carryOutStatus: RestaurantStatus,
  deliveryStatus: RestaurantStatus,
)
