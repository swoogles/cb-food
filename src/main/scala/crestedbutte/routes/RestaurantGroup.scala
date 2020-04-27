package crestedbutte.routes

import crestedbutte.{
  Name,
  Restaurant,
  RestaurantGroupName,
  RestaurantWithStatus,
}

case class RestaurantGroup(
  restaurantGroupName: Name,
  allRestaurants: Seq[Restaurant],
)
