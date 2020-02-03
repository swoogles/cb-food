package crestedbutte

// This class should be usable *without* needing access to the Clock.
case class UpcomingArrivalInfo(
  location: StopLocation.Value,
  content: Either[StopTimeInfo, SafeRideRecommendation]
  /* TODO: waitDuration: Duration*/
)
