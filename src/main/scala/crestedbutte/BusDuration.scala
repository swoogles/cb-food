package crestedbutte

import java.time.Duration

class BusDuration(duration: Duration) {
  def toMinutes: Long = duration.toMinutes

  def dividedByMinutes(minutes: Int) =
    toMinutes / java.time.Duration.ofMinutes(15).toMinutes
}
