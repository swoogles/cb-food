package crestedbutte.time

case class DailyHours(open: BusTime, close: BusTime)

object DailyHours {

  def apply(open: String, close: String): DailyHours =
    DailyHours(BusTime(open), BusTime(close))
}
