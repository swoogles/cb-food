package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}

case class BusScheduleAtStop(location: Location.Value,
                             times: Seq[BusTime]) {

  def timesDelayedBy(busDuration: BusDuration,
                     locationIn: Location.Value) =
    BusScheduleAtStop(locationIn, times.map(_.plus(busDuration)))

  def delayedBy(busDuration: BusDuration) =
    BusScheduleAtStop(location, times.map(_.plus(busDuration)))

  def at(locationIn: Location.Value) =
    BusScheduleAtStop(locationIn, times)

}

object BusScheduleAtStop {

  def apply(location: Location.Value,
            scheduleAtStop: BusSchedule): BusScheduleAtStop =
    BusScheduleAtStop(location, scheduleAtStop.stopTimes)
}
