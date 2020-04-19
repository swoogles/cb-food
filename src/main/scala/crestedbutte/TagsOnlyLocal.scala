package crestedbutte

import java.time.format.TextStyle
import java.util.Locale

import crestedbutte.dom.Bulma
import crestedbutte.time.{
  BusDuration,
  BusTime,
  ClosedForTheDay,
  DailyHours,
  DailyInfo,
  HoursOfOperation,
}
import org.scalajs.dom.html.{Anchor, Div}
import scalatags.JsDom
import scalatags.JsDom.TypedTag

object TagsOnlyLocal {
  import scalatags.JsDom.all._

  def overallPageLayout(pageMode: AppMode.Value,
                        allComponentData: Seq[ComponentData]) =
    div(id := "container")(
      Bulma.menu(
        allComponentData.map { componentData =>
          Bulma.Button.anchor(
            componentData.restaurantGroup.restaurantGroupName.userFriendlyName,
          )(data("route") := componentData.componentName)
        },
        "Restaurants",
      ),
      allComponentData.map(
        singleComponentData =>
          busScheduleDiv(singleComponentData.componentName),
      ),
      if (pageMode == AppMode.Development) {
        div(
          Bulma.Button.basic("Request Notifications Permission")(
            id := ElementNames.Notifications.requestPermission,
          ),
          Bulma.Button.basic("SubmitMessage to SW")(
            id := ElementNames.Notifications.submitMessageToServiceWorker,
          ),
        )
      } else div(),
    )

  def busScheduleDiv(containerName: String) =
    div(cls := ElementNames.BoxClass, id := containerName)(
      div(cls := "timezone"),
      div(id := ElementNames.contentName),
    )

  def renderWebsiteLink(
    website: Website,
  ): JsDom.TypedTag[Div] =
    div(cls := "call-button")(
      Bulma.Button.basic(
        div(
          img(
            cls := "glyphicon",
            src := "/glyphicons/svg/individual-svg/glyphicons-basic-417-globe.svg",
            alt := "Visit Website",
          ),
          span(verticalAlign := "top")(
            website.name,
          ),
        ),
      )(
        onclick :=
          s"window.location.href = '${website.url}';",
      ),
    )

  //  <a href="tel:123-456-7890">123-456-7890</a>
  def phoneButton(
    safeRideRecommendation: PhoneNumber,
    openForOrders: Boolean,
  ): JsDom.TypedTag[Div] =
    div(cls := "call-button")(
      Bulma.Button
        .basic(
          div(
            img(
              cls := "glyphicon",
              src := "/glyphicons/svg/individual-svg/glyphicons-basic-465-call.svg",
              alt := "Call Late Night Shuttle!",
            ),
            span(verticalAlign := "top")(
              safeRideRecommendation.name,
            ),
          ),
        )
        .apply(
          onclick :=
            s"window.location.href = 'tel:${safeRideRecommendation.number}';",
          cls := (if (openForOrders)
                    "button is-success" // TODO Should only be submitting extra classes to bulma lib, *not* re-writing them here.
                  else
                    "button"),
        ),
    )

  def phoneLink(
    phoneNumber: PhoneNumber,
  ): JsDom.TypedTag[Anchor] =
    a(href := s"tel:${phoneNumber.number}", cls := "link")(
      phoneNumber.name,
    )

  def renderWaitTime(duration: BusDuration) =
    if (duration.toMinutes == 0)
      "Leaving!"
    else
      duration.toMinutes + " min."

  def renderDeliverySchedule(
    hoursOfOperationOpt: Option[HoursOfOperation],
  ) =
    renderHoursOfOperation(hoursOfOperationOpt, "Delivery")

  def renderPickupSchedule(
    hoursOfOperationOpt: Option[HoursOfOperation],
  ) =
    renderHoursOfOperation(hoursOfOperationOpt, "Pickup")

  def renderHoursOfOperation(
    hoursOfOperationOpt: Option[HoursOfOperation],
    scheduleHeader: String,
  ) =
    div(cls := "hours-of-operation")(
      div(cls := "hours-header")(
        scheduleHeader + " Schedule",
      ),
      hoursOfOperationOpt match {
        case Some(hoursOfOperation) =>
          div(
            renderDailySchedule(hoursOfOperation.sunday),
            renderDailySchedule(hoursOfOperation.monday),
            renderDailySchedule(hoursOfOperation.tuesday),
            renderDailySchedule(hoursOfOperation.wednesday),
            renderDailySchedule(hoursOfOperation.thursday),
            renderDailySchedule(hoursOfOperation.friday),
            renderDailySchedule(hoursOfOperation.saturday),
          )
        case None => div("Not available.")
      },
    )

  def renderDailyhours(dailyHours: DailyHours) =
    div(cls := "daily-hours")(
      dailyHours.dayOfWeek
        .getDisplayName(TextStyle.NARROW, Locale.US) + " " +
      dailyHours.open.toDumbAmericanString + "-" +
      dailyHours.close.toDumbAmericanString,
    )

  def renderDailySchedule(
    dailySchedule: DailyInfo,
  ) =
    dailySchedule match {
      case closedForTheDay: ClosedForTheDay =>
        renderClosedForTheDay(closedForTheDay)
      case dailyHours: DailyHours => renderDailyhours(dailyHours)
    }

  def renderClosedForTheDay(closedForTheDay: ClosedForTheDay) =
    div(cls := "daily-hours")(
      closedForTheDay.dayOfWeek
        .getDisplayName(TextStyle.NARROW, Locale.US) + " " +
      " *CLOSED* ",
    )

  def createBusTimeElement(
    location: Location.Value,
    content: TypedTag[Div],
    website: Website,
    facebookPage: Website,
    businessDetailsOpt: Option[BusinessDetails],
    carryOutStatus: RestaurantStatus,
    deliveryStatus: RestaurantStatus,
  ): JsDom.TypedTag[Div] =
    Bulma.collapsedCardWithHeader(
      div(cls := "restaurant-header")(
        div(cls := "restaurant-name")(
//          if (carryOutStatus == Open || deliveryStatus == Open)
//            div(location.name + "Open now!")
//          else
          div(location.name),
        ),
        div(cls := "restaurant-call")(
          content,
        ),
      ),
      div(
        svgIcon("glyphicons-basic-221-chevron-down.svg"),
      ),
      div(cls := "restaurant-information")(
        businessDetailsOpt
          .map {
            case StandardSchedule(deliveryHours, carryOutHours) =>
              div(cls := "schedule")(
                div(cls := "pickup-schedule")(
                  renderPickupSchedule(carryOutHours),
                ),
                div(cls := "delivery-schedule")(
                  renderDeliverySchedule(deliveryHours),
                ),
              )
            case advanceOrdersOnly: AdvanceOrdersOnly =>
              div(cls := "advance-order-procedure")(
                "Advance order only: " + advanceOrdersOnly.instructions,
              )
          }
          .getOrElse(
            div(
              "Please visit these sites for more detailed information.",
            ),
          ),
      ),
      List(
        renderWebsiteLink(website),
        renderWebsiteLink(facebookPage),
//        renderHoursOfOperation(hoursOfOperation)
      ),
    )(data("location") := location.elementName)

  def activateModal(targetName: String): Unit =
    org.scalajs.dom.document.body
      .querySelector(targetName)
      .classList
      .add("is-active")

  def modalContentElementNameTyped(location: Location.Value,
                                   routeName: RestaurantGroupName) =
    data("schedule-modal") := modalContentElementName(location,
                                                      routeName)

  def modalContentElementName(location: Location.Value,
                              routeName: RestaurantGroupName) =
    "modal_content_" + routeName.name + "_" + location.elementName

  def structuredSetOfUpcomingArrivals(
    upcomingArrivalComponentData: CurrentComponentData,
  ) =
    div(
      div(cls := "route-header")(
        span(cls := "route-header_name")(
          upcomingArrivalComponentData.routeName.userFriendlyName,
        ),
      ),
      upcomingArrivalComponentData.upcomingArrivalInfo.map {
        case RestaurantWithStatus(
            RestaurantWithSchedule(
              location: Location.Value,
              phoneNumber: PhoneNumber,
              website: Website,
              facebookPage: Website,
              businessDetails: Option[BusinessDetails],
            ),
            carryOutStatus,
            deliveryStatus,
            ) => {
          val openForOrders =
            (carryOutStatus == Open || deliveryStatus == Open)
          TagsOnlyLocal.createBusTimeElement(
            location,
            phoneButton(phoneNumber, openForOrders),
            website,
            facebookPage,
            businessDetails,
            carryOutStatus,
            deliveryStatus,
          )
        }
      },
    )

  def svgIconForAlarm(name: String,
                      classes: String,
                      busTime: BusTime) =
    img(
      cls := "glyphicon " + classes,
      src := s"/glyphicons/svg/individual-svg/$name",
      alt := "Thanks for riding the bus!",
      data("lossless-value") := busTime.toString,
      verticalAlign := "middle",
    )

  def svgIcon(name: String) =
    img(
      cls := "glyphicon",
      src := s"/glyphicons/svg/individual-svg/$name",
      alt := "",
    )
  /*
  glyphicons-basic-591-map-marker.svg
  glyphicons-basic-417-globe.svg
  glyphicons-basic-262-direction-empty.svg
  glyphicons-basic-581-directions.svg
  glyphicons-basic-697-directions-sign.svg

 */

}
