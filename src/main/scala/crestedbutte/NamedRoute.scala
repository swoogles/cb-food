package crestedbutte

import crestedbutte.routes.RestaurantGroup

case class NamedRoute(routeName: RouteName,
                      routeWithTimes: RestaurantGroup) {}

object NamedRoute {

  def apply(rawRouteName: String,
            routeWithTimes: RestaurantGroup): Unit =
    NamedRoute(
      new RouteName(rawRouteName),
      routeWithTimes,
    )
}
