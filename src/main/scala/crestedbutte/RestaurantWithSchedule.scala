package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}

case class RestaurantWithSchedule(location: Location.Value,
                                  times: Seq[BusTime],
                                  phoneNumber: PhoneNumber,
                                  website: Website) {

  def timesDelayedBy(busDuration: BusDuration,
                     locationIn: Location.Value) =
    RestaurantWithSchedule(locationIn,
                           times.map(_.plus(busDuration)),
                           phoneNumber,
                           website)

  def delayedBy(busDuration: BusDuration) =
    RestaurantWithSchedule(location,
                           times.map(_.plus(busDuration)),
                           phoneNumber,
                           website)

  def at(locationIn: Location.Value) =
    RestaurantWithSchedule(locationIn, times, phoneNumber, website)

  def scheduleAfter(busTime: BusTime) =
    RestaurantWithSchedule(
      location,
      times.dropWhile(!BusTime.catchableBus(busTime, _)),
      phoneNumber,
      website,
    )
}

object RestaurantWithSchedule {

  def apply(location: Location.Value,
            scheduleAtStop: BusSchedule,
            phoneNumber: PhoneNumber,
            website: Website): RestaurantWithSchedule =
    RestaurantWithSchedule(location,
                           scheduleAtStop.stopTimes,
                           phoneNumber,
                           website)

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
        schedule1.phoneNumber,
        schedule1.website,
      ) // TODO Ensure sorted times

}
