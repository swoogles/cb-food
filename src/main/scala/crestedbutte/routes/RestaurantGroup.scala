package crestedbutte.routes

import crestedbutte.{
  RestaurantGroupName,
  RestaurantWithSchedule,
  RestaurantWithStatus,
}

case class RestaurantGroup(
  restaurantGroupName: RestaurantGroupName,
  allRestaurants: Seq[RestaurantWithSchedule],
)
