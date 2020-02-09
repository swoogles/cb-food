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

  private val dateFormat = DateTimeFormatter.ofPattern("h:mm")

  override def toString: String = localTime.format(dateFormat)

}
