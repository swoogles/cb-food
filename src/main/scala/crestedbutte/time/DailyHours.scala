package crestedbutte.time

import java.time.DayOfWeek

import com.billding.time.BusTime

sealed trait DailyInfo

case class DailyHoursSegment(open: BusTime, close: BusTime)

case class DailyHours(hoursSegment: Seq[DailyHoursSegment],
                      dayOfWeek: DayOfWeek)
    extends DailyInfo {

//  def isOpenNow(now: BusTime) =
//    open.isBeforeOrEqualTo(now) || close.isAfterOrEqualTo(now)
}

object DailyHours {

  def apply(open: BusTime,
            close: BusTime,
            dayOfWeek: DayOfWeek): DailyHours =
    DailyHours(Seq(DailyHoursSegment(open, close)), dayOfWeek)

  def apply(open: BusTime,
            close: BusTime,
            open2: BusTime,
            close2: BusTime,
            dayOfWeek: DayOfWeek): DailyHours =
    DailyHours(Seq(DailyHoursSegment(open, close),
                   DailyHoursSegment(open2, close2)),
               dayOfWeek)

  def apply(open: String,
            close: String,
            dayOfWeek: DayOfWeek): DailyHours =
    DailyHours(BusTime(open), BusTime(close), dayOfWeek)

  def apply(open: String,
            close: String,
            open2: String,
            close2: String,
            dayOfWeek: DayOfWeek): DailyHours =
    DailyHours(BusTime(open),
               BusTime(close),
               BusTime(open2),
               BusTime(close2),
               dayOfWeek)
}

case class ClosedForTheDay(dayOfWeek: DayOfWeek) extends DailyInfo
