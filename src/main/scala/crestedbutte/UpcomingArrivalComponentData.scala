package crestedbutte

case class UpcomingArrivalComponentData(
  upcomingArrivalInfo: Seq[UpcomingArrivalInfoWithFullSchedule],
  routeName: RouteName
)
