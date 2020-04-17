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
  ): JsDom.TypedTag[Div] =
    div(cls := "call-button")(
      Bulma.Button.basic(
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
      )(
        onclick :=
          s"window.location.href = 'tel:${safeRideRecommendation.number}';",
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

  def renderDeliverySchedule(hoursOfOperation: HoursOfOperation) =
    renderHoursOfOperation(hoursOfOperation, "Delivery")

  def renderPickupSchedule(hoursOfOperation: HoursOfOperation) =
    renderHoursOfOperation(hoursOfOperation, "Pickup")

  def renderHoursOfOperation(hoursOfOperation: HoursOfOperation,
                             scheduleHeader: String) =
    div(cls := "hours-of-operation")(
      div(cls := "hours-header")(
        scheduleHeader + " Schedule",
      ),
      renderDailySchedule(hoursOfOperation.sunday),
      renderDailySchedule(hoursOfOperation.monday),
      renderDailySchedule(hoursOfOperation.tuesday),
      renderDailySchedule(hoursOfOperation.wednesday),
      renderDailySchedule(hoursOfOperation.thursday),
      renderDailySchedule(hoursOfOperation.friday),
      renderDailySchedule(hoursOfOperation.saturday),
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
  ): JsDom.TypedTag[Div] =
    Bulma.collapsedCardWithHeader(
      div(cls := "restaurant-header")(
        div(cls := "restaurant-name")(
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
              div(cls := "schedule")( // TODO Orrrr do the advance order text here
                div(cls := "pickup-schedule")(
                  carryOutHours.map(renderPickupSchedule),
                ),
                div(cls := "delivery-schedule")(
                  deliveryHours.map(renderDeliverySchedule),
                ),
              )
            case advanceOrdersOnly: AdvanceOrdersOnly =>
              div(cls := "advance-order-procedure")(
                "Advance order only: " + advanceOrdersOnly.instructions,
              )
          }
          .getOrElse(
            div(
              "No details available for this restaurant. Please visit their sites directly.",
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
    upcomingArrivalComponentData: UpcomingArrivalComponentData,
  ) =
    div(
      div(cls := "route-header")(
        span(cls := "route-header_name")(
          upcomingArrivalComponentData.routeName.userFriendlyName,
        ),
      ),
      upcomingArrivalComponentData.upcomingArrivalInfo.map {
        case RestaurantWithSchedule(
            location: Location.Value,
            scheduleAtStop: BusSchedule,
            phoneNumber: PhoneNumber,
            website: Website,
            facebookPage: Website,
            businessDetails: Option[BusinessDetails],
            ) => {
          TagsOnlyLocal.createBusTimeElement(location,
                                             phoneButton(phoneNumber),
                                             website,
                                             facebookPage,
                                             businessDetails)
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
