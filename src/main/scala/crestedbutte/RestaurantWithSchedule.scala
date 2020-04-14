package crestedbutte

import crestedbutte.time.{BusDuration, BusTime, HoursOfOperation}

case class RestaurantWithSchedule(
  location: Location.Value,
  scheduleAtStop: BusSchedule,
  phoneNumber: PhoneNumber,
  website: Website,
  facebookPage: Website,
  deliveryHours: Option[HoursOfOperation] = None,
) {}
