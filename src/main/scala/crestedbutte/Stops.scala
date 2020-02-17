package crestedbutte

import crestedbutte.time.BusTime

case class Stops(location: StopLocation.Value, times: Seq[BusTime])

object Stops {}
