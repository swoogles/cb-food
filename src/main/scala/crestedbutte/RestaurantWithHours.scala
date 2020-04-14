package crestedbutte

import crestedbutte.time.BusTime

case class RestaurantWithHours(location: Location.Value,
                               times: Seq[BusTime])

object RestaurantWithHours {

  def apply(location: Location.Value,
            scheduleAtStop: BusSchedule): RestaurantWithHours =
    RestaurantWithHours(location, scheduleAtStop.stopTimes)

}
