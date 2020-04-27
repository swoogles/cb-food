package crestedbutte.routes

import crestedbutte.{
  Restaurant,
  RestaurantGroupName,
  RestaurantWithStatus,
}

case class RestaurantGroup(
  restaurantGroupName: RestaurantGroupName,
  allRestaurants: Seq[Restaurant],
)
