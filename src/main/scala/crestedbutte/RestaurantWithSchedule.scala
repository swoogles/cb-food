package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}

case class RestaurantWithSchedule(location: Location.Value,
                                  times: Seq[BusTime]) {

  def timesDelayedBy(busDuration: BusDuration,
                     locationIn: Location.Value) =
    RestaurantWithSchedule(locationIn, times.map(_.plus(busDuration)))

  def delayedBy(busDuration: BusDuration) =
    RestaurantWithSchedule(location, times.map(_.plus(busDuration)))

  def at(locationIn: Location.Value) =
    RestaurantWithSchedule(locationIn, times)

  def scheduleAfter(busTime: BusTime) =
    RestaurantWithSchedule(
      location,
      times.dropWhile(!BusTime.catchableBus(busTime, _)),
    )
}

object RestaurantWithSchedule {

  def apply(location: Location.Value,
            scheduleAtStop: BusSchedule): RestaurantWithSchedule =
    RestaurantWithSchedule(location, scheduleAtStop.stopTimes)

  def combine(
    schedule1: RestaurantWithSchedule,
    schedule2: RestaurantWithSchedule,
  ): RestaurantWithSchedule =
    if (schedule1.location != schedule2.location)
      throw new RuntimeException("Blah")
    else
      RestaurantWithSchedule(
        schedule1.location,
        (schedule1.times ++ schedule2.times).sortBy(_.toString),
      ) // TODO Ensure sorted times

}
