package crestedbutte

// This class should be usable *without* needing access to the Clock.
case class UpcomingArrivalInfo(
  location: Location.Value,
  content: Either[StopTimeInfo, CallToOrder],
  /* TODO: waitDuration: Duration*/
)

object UpcomingArrivalInfo {

  def apply(
    location: Location.Value,
    content: StopTimeInfo,
  ): UpcomingArrivalInfo =
    UpcomingArrivalInfo(
      location,
      Left(
        content,
      ),
    )

  def apply(
    location: Location.Value,
    content: CallToOrder,
  ): UpcomingArrivalInfo =
    UpcomingArrivalInfo(
      location,
      Right(content),
    )

}
