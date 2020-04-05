package crestedbutte

case class UpcomingArrivalInfoWithFullSchedule(
  upcomingArrivalInfo: UpcomingArrivalInfo,
  busScheduleAtStop: RestaurantWithSchedule,
) {}
