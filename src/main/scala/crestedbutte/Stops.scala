package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}

case class Stops(location: StopLocation.Value, times: Seq[BusTime]) {

  def timesDelayedBy(busDuration: BusDuration,
                     locationIn: StopLocation.Value) =
    Stops(locationIn, times.map(_.plus(busDuration)))

  def delayedBy(busDuration: BusDuration) =
    Stops(location, times.map(_.plus(busDuration)))

  def at(locationIn: StopLocation.Value) =
    Stops(locationIn, times)

}

object Stops {}
