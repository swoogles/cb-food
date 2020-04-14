package crestedbutte.time

case class HoursOfOperation(
  sunday: DailyHours,
  monday: DailyHours,
  tuesday: DailyHours,
  wednesday: DailyHours,
  thursday: DailyHours,
  friday: DailyHours,
  saturday: DailyHours,
)

object HoursOfOperation {

  def apply(hours: DailyHours): HoursOfOperation =
    HoursOfOperation(
      sunday = hours,
      monday = hours,
      tuesday = hours,
      wednesday = hours,
      thursday = hours,
      friday = hours,
      saturday = hours,
    )
}
