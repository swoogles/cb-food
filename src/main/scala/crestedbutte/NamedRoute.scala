package crestedbutte

import crestedbutte.routes.RestaurantGroup

case class NamedRoute(routeName: RestaurantGroupName,
                      routeWithTimes: RestaurantGroup)
