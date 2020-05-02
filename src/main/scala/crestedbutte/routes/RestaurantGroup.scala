package crestedbutte.routes

import crestedbutte.{Name, Restaurant}

case class RestaurantGroup(
  name: Name,
  allRestaurants: Seq[Restaurant],
) {
  val componentName = name.elementName
}
