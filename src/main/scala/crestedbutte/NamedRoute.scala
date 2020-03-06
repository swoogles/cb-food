package crestedbutte

case class NamedRoute(routeName: RouteName.Value,
                      schedules: Seq[BusScheduleAtStop]) {}
