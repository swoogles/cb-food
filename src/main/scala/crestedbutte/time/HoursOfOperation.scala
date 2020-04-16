package crestedbutte.time

import java.time.DayOfWeek

case class HoursOfOperation(
  sunday: Either[ClosedForTheDay, DailyHours],
  monday: Either[ClosedForTheDay, DailyHours],
  tuesday: Either[ClosedForTheDay, DailyHours],
  wednesday: Either[ClosedForTheDay, DailyHours],
  thursday: Either[ClosedForTheDay, DailyHours],
  friday: Either[ClosedForTheDay, DailyHours],
  saturday: Either[ClosedForTheDay, DailyHours],
)

object HoursOfOperation {

  def apply(open: String, close: String): HoursOfOperation =
    HoursOfOperation(
      sunday = Right(DailyHours(open, close, DayOfWeek.SUNDAY)),
      monday = Right(DailyHours(open, close, DayOfWeek.MONDAY)),
      tuesday = Right(DailyHours(open, close, DayOfWeek.TUESDAY)),
      wednesday = Right(DailyHours(open, close, DayOfWeek.WEDNESDAY)),
      thursday = Right(DailyHours(open, close, DayOfWeek.THURSDAY)),
      friday = Right(DailyHours(open, close, DayOfWeek.FRIDAY)),
      saturday = Right(DailyHours(open, close, DayOfWeek.SATURDAY)),
    )
}
