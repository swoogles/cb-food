package crestedbutte.time

import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalTime}

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

  def plus(busDuration: BusDuration) =
    new BusTime(localTime.plusMinutes(busDuration.toMinutes))

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

  def apply(raw: String) =
    parse(raw)

  def parse(raw: String) =
    try {
      new BusTime(LocalTime.parse(raw, dateFormat))
    } catch {
      case ex: DateTimeParseException =>
        try {
          println("Failed default parse: " + ex.getParsedString)
          println("trying again with a leading 0")
          new BusTime(LocalTime.parse("0" + raw))
        } catch {
          case ex: DateTimeParseException =>
            println("Failed default parse: " + ex.getParsedString)
            new BusTime(LocalTime.parse(raw))
        }
    }

  def catchableBus(now: BusTime, goal: BusTime) =
    goal.truncatedToMinutes
      .isAfter(now.truncatedToMinutes) ||
    goal.truncatedToMinutes
      .equals(now.truncatedToMinutes)

}
