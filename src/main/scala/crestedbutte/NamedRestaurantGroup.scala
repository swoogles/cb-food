package crestedbutte

import crestedbutte.routes.RestaurantGroup

case class NamedRestaurantGroup(routeName: RestaurantGroupName,
                                restaurantGroup: RestaurantGroup)
