package crestedbutte.time

import java.time.DayOfWeek

sealed trait ScheduleInput {
  def on(dayOfWeek: DayOfWeek): DailyInfo
}

case class HoursGrouping(hourGroups: Seq[Hours])
    extends ScheduleInput {

  def on(dayOfWeek: DayOfWeek): DailyHours =
    DailyHours(hourGroups.map(
                 hours => DailyHoursSegment(hours.open, hours.close),
               ),
               dayOfWeek)
}

case class Hours(open: BusTime, close: BusTime)
    extends ScheduleInput {

  def isOpenNow(now: BusTime) =
    open.isBeforeOrEqualTo(now) || close.isAfterOrEqualTo(now)

  def on(dayOfWeek: DayOfWeek): DailyHours =
    DailyHours(open, close, dayOfWeek)
}

object Hours {

  def apply(open: String, close: String): Hours =
    Hours(BusTime(open), BusTime(close))

  def apply(open: String,
            close: String,
            open2: String,
            close2: String): Seq[Hours] =
    Seq(
      Hours(BusTime(open), BusTime(close)),
      Hours(BusTime(open2), BusTime(close2)),
    )
}

case object ClosedAllDay extends ScheduleInput {

  def on(dayOfWeek: DayOfWeek): ClosedForTheDay =
    ClosedForTheDay(dayOfWeek)
}
