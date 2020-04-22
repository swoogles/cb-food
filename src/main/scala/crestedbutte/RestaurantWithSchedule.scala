package crestedbutte

import java.time.DayOfWeek

import crestedbutte.time.{BusDuration, BusTime, HoursOfOperation}

case class RestaurantWithSchedule(
  location: Location,
  externalActions: ExternalActionCollection,
  businessDetails: Option[BusinessDetails] = None,
)

object RestaurantWithSchedule {

  def apply(
    location: Location,
    phoneNumber: PhoneNumber,
    website: Website,
    facebookPage: Website,
    businessDetails: Option[BusinessDetails],
  ): RestaurantWithSchedule = RestaurantWithSchedule(
    location,
    ExternalActionCollection(
      CallLocation(phoneNumber),
      Seq(VisitHomePage(website), VisitFacebookPage(facebookPage)),
    ),
    businessDetails,
  )

  def apply(
    location: Location,
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
    location: Location,
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
  deliveryHours: Option[HoursOfOperation],
  carryOutHours: Option[HoursOfOperation],
) extends BusinessDetails

object StandardSchedule {

  def carryOutOnly(
    carryOutHours: HoursOfOperation,
  ): StandardSchedule =
    StandardSchedule(
      None,
      Some(carryOutHours),
    )

  def apply(
    deliveryHours: HoursOfOperation,
    carryOutHours: HoursOfOperation,
  ): StandardSchedule =
    StandardSchedule(
      Some(deliveryHours),
      Some(carryOutHours),
    )

  def carryOutAndDelivery(
    hoursOfOperation: HoursOfOperation,
  ) =
    StandardSchedule(
      Some(hoursOfOperation),
      Some(hoursOfOperation),
    )

}
