package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}

case class BusScheduleAtStop(location: StopLocation.Value, times: Seq[BusTime]) {

  def timesDelayedBy(busDuration: BusDuration,
                     locationIn: StopLocation.Value) =
    BusScheduleAtStop(locationIn, times.map(_.plus(busDuration)))

  def delayedBy(busDuration: BusDuration) =
    BusScheduleAtStop(location, times.map(_.plus(busDuration)))

  def at(locationIn: StopLocation.Value) =
    BusScheduleAtStop(locationIn, times)

}

object BusScheduleAtStop {
  def apply(location: StopLocation.Value, scheduleAtStop: BusSchedule): BusScheduleAtStop =
    BusScheduleAtStop(location, scheduleAtStop.stopTimes)
}
