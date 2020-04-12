package crestedbutte

import crestedbutte.time.BusTime

case class BusScheduleAtStop(location: Location.Value,
                             times: Seq[BusTime])

object BusScheduleAtStop {

  def apply(location: Location.Value,
            scheduleAtStop: BusSchedule): BusScheduleAtStop =
    BusScheduleAtStop(location, scheduleAtStop.stopTimes)

}
