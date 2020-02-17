package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}

case class Stops(location: StopLocation.Value, times: Seq[BusTime]) {

  def timesDelayedBy(busDuration: BusDuration,
                     location: StopLocation.Value) =
    Stops(StopLocation.TeocalliUphill, times.map(_.plus(busDuration)))

}

object Stops {}
