package crestedbutte.routes

import crestedbutte.{Name, Restaurant}

case class RestaurantGroup(
  restaurantGroupName: Name,
  allRestaurants: Seq[Restaurant],
)
