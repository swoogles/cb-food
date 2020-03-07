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

  def scheduleAfter(busTime: BusTime) =
    BusScheduleAtStop(
      location,
      times.dropWhile(!BusTime.catchableBus(busTime, _)),
    )
}

object BusScheduleAtStop {

  def apply(location: Location.Value,
            scheduleAtStop: BusSchedule): BusScheduleAtStop =
    BusScheduleAtStop(location, scheduleAtStop.stopTimes)

  def combine(schedule1: BusScheduleAtStop,
              schedule2: BusScheduleAtStop): BusScheduleAtStop =
    if (schedule1.location != schedule2.location)
      throw new RuntimeException("Blah")
    else
      BusScheduleAtStop(
        schedule1.location,
        (schedule1.times ++ schedule2.times).sortBy(_.toString),
      ) // TODO Ensure sorted times

}
