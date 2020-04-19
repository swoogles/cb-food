package crestedbutte.time

import java.time.DayOfWeek

sealed trait DailyInfo

case class DailyHours(open: BusTime,
                      close: BusTime,
                      dayOfWeek: DayOfWeek)
    extends DailyInfo {

  def isOpenNow(now: BusTime) =
    open.isBeforeOrEqualTo(now) || close.isAfterOrEqualTo(now)
}

object DailyHours {

  def apply(open: String,
            close: String,
            dayOfWeek: DayOfWeek): DailyHours =
    DailyHours(BusTime(open), BusTime(close), dayOfWeek)
}

case class ClosedForTheDay(dayOfWeek: DayOfWeek) extends DailyInfo
