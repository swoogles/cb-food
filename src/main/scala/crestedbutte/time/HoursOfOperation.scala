package crestedbutte.time

import java.time.DayOfWeek

case class HoursOfOperation(
  sunday: DailyInfo,
  monday: DailyInfo,
  tuesday: DailyInfo,
  wednesday: DailyInfo,
  thursday: DailyInfo,
  friday: DailyInfo,
  saturday: DailyInfo,
) {

  def dailyInfoFor(dayOfWeek: DayOfWeek): DailyInfo =
    if (dayOfWeek == DayOfWeek.SUNDAY)
      sunday
    else if (dayOfWeek == DayOfWeek.MONDAY)
      monday
    else if (dayOfWeek == DayOfWeek.TUESDAY)
      tuesday
    else if (dayOfWeek == DayOfWeek.WEDNESDAY)
      wednesday
    else if (dayOfWeek == DayOfWeek.THURSDAY)
      thursday
    else if (dayOfWeek == DayOfWeek.FRIDAY)
      friday
    else if (dayOfWeek == DayOfWeek.SATURDAY)
      saturday
    else
      throw new IllegalArgumentException(
        "WTF is this day? day: " + dayOfWeek,
      )
}

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

  def apply(
    sunday: ScheduleInput,
    monday: ScheduleInput,
    tuesday: ScheduleInput,
    wednesday: ScheduleInput,
    thursday: ScheduleInput,
    friday: ScheduleInput,
    saturday: ScheduleInput,
  ): HoursOfOperation =
    HoursOfOperation(
      sunday = sunday.on(DayOfWeek.SUNDAY),
      monday = monday.on(DayOfWeek.MONDAY),
      tuesday = tuesday.on(DayOfWeek.TUESDAY),
      wednesday = wednesday.on(DayOfWeek.WEDNESDAY),
      thursday = thursday.on(DayOfWeek.THURSDAY),
      friday = friday.on(DayOfWeek.FRIDAY),
      saturday = saturday.on(DayOfWeek.SATURDAY),
    )
}
