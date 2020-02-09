package crestedbutte

import java.time.Duration

class BusDuration(duration: Duration) {
  def toMinutes: Long = duration.toMinutes
}
