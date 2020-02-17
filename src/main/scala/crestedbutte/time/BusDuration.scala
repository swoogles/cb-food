package crestedbutte.time

import java.time.Duration

class BusDuration(duration: Duration) {
  def toMinutes: Long = duration.toMinutes

  def dividedBy(duration: BusDuration) =
    toMinutes / duration.toMinutes
}

object BusDuration {

  def ofMinutes(minutes: Int) =
    new BusDuration(java.time.Duration.ofMinutes(minutes))

  class DurationFriendlyInt(int: Int) {
    def minutes: BusDuration = BusDuration.ofMinutes(int)
  }

  implicit def toBusDuration(int: Int) =
    new DurationFriendlyInt(int)
}
