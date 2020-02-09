package crestedbutte

import java.time.LocalTime

case class Stops(location: StopLocation.Value, times: Seq[BusTime])

object Stops {

  def fromJavaTimes(location: StopLocation.Value,
                    times: Seq[LocalTime]) =
    Stops(location, times.map(time => new BusTime(time)))

}
