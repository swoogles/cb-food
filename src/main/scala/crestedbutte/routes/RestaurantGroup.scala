package crestedbutte.routes

import crestedbutte.{RestaurantGroupName, RestaurantWithSchedule}

case class RestaurantGroup(
  restaurantGroupName: RestaurantGroupName,
  allRestaurants: Seq[RestaurantWithSchedule],
)
