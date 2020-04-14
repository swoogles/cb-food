package crestedbutte.time

import java.time.DayOfWeek

case class DailyHours(open: BusTime,
                      close: BusTime,
                      dayOfWeek: DayOfWeek) {

  def isOpenNow(now: BusTime) =
    open.isBeforeOrNow(now) || close.isAfterOrNow(now)
}

object DailyHours {

  def apply(open: String,
            close: String,
            dayOfWeek: DayOfWeek): DailyHours =
    DailyHours(BusTime(open), BusTime(close), dayOfWeek)
}
