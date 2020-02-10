package crestedbutte

import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.{Duration, LocalTime}
import java.time.temporal.ChronoUnit

class BusTime(localTime: LocalTime) {

  private val truncatedToMinutes =
    localTime.truncatedTo(ChronoUnit.MINUTES)

  def between(busTime: BusTime): BusDuration =
    new BusDuration(
      Duration
        .between(
          busTime.truncatedToMinutes,
          truncatedToMinutes
        )
        .abs()
    )

  def plusMinutes(minutes: Int) =
    new BusTime(localTime.plusMinutes(minutes))

  private val dateFormat = DateTimeFormatter.ofPattern("h:mm")

  override val toString: String = localTime.format(dateFormat)

  val tooLateToBeConsideredLateNight: Boolean =
    localTime.isAfter(LocalTime.parse("04:00:00"))

  def canEqual(other: Any): Boolean = other.isInstanceOf[BusTime]

  override def equals(other: Any): Boolean = other match {
    case that: BusTime =>
      (that.canEqual(this)) &&
      truncatedToMinutes == that.truncatedToMinutes
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(truncatedToMinutes)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object BusTime {
  private val dateFormat = DateTimeFormatter.ofPattern("h:mm")

  def parse(raw: String) =
    try {
      new BusTime(LocalTime.parse(raw, dateFormat))
    } catch {
      case _: DateTimeParseException =>
        new BusTime(LocalTime.parse(raw))
    }

  def catchableBus(now: BusTime, goal: BusTime) =
    goal.truncatedToMinutes
      .isAfter(now.truncatedToMinutes) ||
    goal.truncatedToMinutes
      .equals(now.truncatedToMinutes)

}
