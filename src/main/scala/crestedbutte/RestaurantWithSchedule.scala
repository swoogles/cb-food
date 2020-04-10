package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}

case class RestaurantWithSchedule(location: Location.Value,
                                  times: Seq[BusTime],
                                  phoneNumber: PhoneNumber,
                                  website: Website,
                                  facebookPage: Website) {}

object RestaurantWithSchedule {

  def apply(location: Location.Value,
            scheduleAtStop: BusSchedule,
            phoneNumber: PhoneNumber,
            website: Website,
            facebookPage: Website): RestaurantWithSchedule =
    RestaurantWithSchedule(location,
                           scheduleAtStop.stopTimes,
                           phoneNumber,
                           website,
                           facebookPage)

}
