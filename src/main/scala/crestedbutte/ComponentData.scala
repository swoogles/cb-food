package crestedbutte

import crestedbutte.routes.RestaurantGroup

case class ComponentData(
  restaurantGroup: RestaurantGroup,
) {
  val componentName = restaurantGroup.restaurantGroupName.elementName
}
