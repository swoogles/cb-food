package crestedbutte

case class UpcomingArrivalInfoWithFullSchedule(
  upcomingArrivalInfo: RestaurantWithStatus,
  busScheduleAtStop: RestaurantWithSchedule,
) {}
