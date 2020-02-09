package crestedbutte

import java.time.format.DateTimeFormatter
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

}

object BusTime {

  def catchableBus(now: BusTime, goal: BusTime) =
    goal.truncatedToMinutes
      .isAfter(now.truncatedToMinutes) ||
    goal.truncatedToMinutes
      .equals(now.truncatedToMinutes)

}
