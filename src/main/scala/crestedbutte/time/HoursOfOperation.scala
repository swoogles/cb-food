package crestedbutte.time

import java.time.DayOfWeek

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

  def apply(open: String, close: String): HoursOfOperation =
    HoursOfOperation(
      sunday = DailyHours(open, close, DayOfWeek.SUNDAY),
      monday = DailyHours(open, close, DayOfWeek.MONDAY),
      tuesday = DailyHours(open, close, DayOfWeek.TUESDAY),
      wednesday = DailyHours(open, close, DayOfWeek.WEDNESDAY),
      thursday = DailyHours(open, close, DayOfWeek.THURSDAY),
      friday = DailyHours(open, close, DayOfWeek.FRIDAY),
      saturday = DailyHours(open, close, DayOfWeek.SATURDAY),
    )
}
