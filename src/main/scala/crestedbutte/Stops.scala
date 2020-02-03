package crestedbutte

import java.time.LocalTime

case class Stops(location: StopLocation.Value, times: Seq[LocalTime])
