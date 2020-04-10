package crestedbutte

case class UpcomingArrivalComponentData(
  upcomingArrivalInfo: Seq[RestaurantWithSchedule],
  routeName: RouteName,
)
