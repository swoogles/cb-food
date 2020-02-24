package crestedbutte

case class Route(schedules: Seq[BusScheduleAtStop],
                 routeName: RouteName.Value) {}
