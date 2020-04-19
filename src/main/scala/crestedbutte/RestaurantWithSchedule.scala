package crestedbutte

import java.time.DayOfWeek

import crestedbutte.time.{BusDuration, BusTime, HoursOfOperation}

case class RestaurantWithSchedule(
  location: Location.Value,
  phoneNumber: PhoneNumber,
  website: Website,
  facebookPage: Website,
  businessDetails: Option[BusinessDetails] = None,
) {}

object RestaurantWithSchedule {

  def apply(
    location: Location.Value,
    phoneNumber: PhoneNumber,
    website: Website,
    facebookPage: Website,
    advancedOrdersOnly: AdvanceOrdersOnly,
  ): RestaurantWithSchedule =
    RestaurantWithSchedule(
      location,
      phoneNumber,
      website,
      facebookPage,
      Some(advancedOrdersOnly),
    )

  def apply(
    location: Location.Value,
    phoneNumber: PhoneNumber,
    website: Website,
    facebookPage: Website,
    standardSchedule: StandardSchedule,
  ): RestaurantWithSchedule =
    RestaurantWithSchedule(
      location,
      phoneNumber,
      website,
      facebookPage,
      Some(standardSchedule),
    )
}

trait BusinessDetails

case class AdvanceOrdersOnly(
  //  orderCutoff: (BusTime, DayOfWeek),
  //  pickupTime: (BusTime, DayOfWeek)
  instructions: String,
) extends BusinessDetails

case class StandardSchedule(
  deliveryHours: Option[HoursOfOperation] = None,
  carryOutHours: Option[HoursOfOperation] = None,
) extends BusinessDetails
