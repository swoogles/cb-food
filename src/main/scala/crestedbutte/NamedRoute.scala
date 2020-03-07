package crestedbutte

import crestedbutte.routes.RouteWithTimes

case class NamedRoute(routeName: RouteName,
                      routeWithTimes: RouteWithTimes) {}

object NamedRoute {

  def apply(rawRouteName: String,
            routeWithTimes: RouteWithTimes): Unit =
    NamedRoute(
      new RouteName(rawRouteName),
      routeWithTimes
    )
}
