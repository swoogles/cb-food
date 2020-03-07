package crestedbutte

import crestedbutte.routes.RouteWithTimes

case class NamedRoute(routeName: RouteName.Value,
                      routeWithTimes: RouteWithTimes) {}
