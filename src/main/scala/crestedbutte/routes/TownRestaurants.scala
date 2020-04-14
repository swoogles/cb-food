package crestedbutte.routes

import crestedbutte.NamedRoute

class TownRestaurants(
  name: String,
  val restaurantGroups: Seq[RestaurantGroup],
) {}
