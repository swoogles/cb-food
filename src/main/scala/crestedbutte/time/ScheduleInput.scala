package crestedbutte.time

import java.time.DayOfWeek

sealed trait ScheduleInput {
  def on(dayOfWeek: DayOfWeek): DailyInfo
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
}

case object ClosedAllDay extends ScheduleInput {

  def on(dayOfWeek: DayOfWeek): ClosedForTheDay =
    ClosedForTheDay(dayOfWeek)
}
