package crestedbutte

import java.time.DayOfWeek
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
      // TODO Restore menu once Gunnison is added
      Bulma.menu(
        allComponentData.map { componentData =>
          Bulma.Button.anchor(
            componentData.restaurantGroup.restaurantGroupName.humanFriendlyName,
          )(data("route") := componentData.componentName)
        },
        "Restaurants",
      ),
      allComponentData.map(
        singleComponentData =>
          busScheduleDiv(singleComponentData.componentName),
      ),
      div(cls := "contact-me")(
        div(
          span("Created by "),
          a(href := "https://www.billdingsoftware.com")(
            "Billding Software LLC",
          ),
        ),
        div(
          "For corrections, confusion, kudos, or anger: ",
        ),
        Bulma.Button.basic(
          a(href := "mailto:bill@billdingsoftware.com")("Email Me!"),
        ),
      ),
      if (pageMode == AppMode.Development) {
        div(
          // Dev stuff here.
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
    openForOrders: Boolean,
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
        cls := (if (openForOrders && website.name == "Order") // TODO ugh. Stringly typed logic in rendering
                  "button is-success" // TODO Should only be submitting extra classes to bulma lib, *not* re-writing them here.
                else
                  "button"),
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
        div(cls := "hours-header-top")(scheduleHeader),
        div(cls := "hours-header-bottom")("Schedule"),
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
        case None =>
          div(cls := "service-unavailable")("Not available.")
      },
    )

  def renderDayName(dayOfWeek: DayOfWeek): String =
    // This doesn't work for some lame reason.
    // .getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US),
    dayOfWeek match {
      case DayOfWeek.SUNDAY    => "Sun"
      case DayOfWeek.MONDAY    => "Mon"
      case DayOfWeek.TUESDAY   => "Tue"
      case DayOfWeek.WEDNESDAY => "Wed"
      case DayOfWeek.THURSDAY  => "Thur"
      case DayOfWeek.FRIDAY    => "Fri"
      case DayOfWeek.SATURDAY  => "Sat"
    }

  def renderDailyhours(dailyHours: DailyHours) =
    div(cls := "daily-hours")(
      div(cls := "day")(
        div(cls := "day-name")(
          renderDayName(dailyHours.dayOfWeek),
        ),
      ),
      div(cls := "hours")(
        dailyHours.hoursSegment.map(
          hoursSegment =>
            div(
              hoursSegment.open.toDumbAmericanString + "-" +
              hoursSegment.close.toDumbAmericanString,
            ),
        ),
      ),
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
      div(cls := "day")(
        div(cls := "day-name")(
          renderDayName(closedForTheDay.dayOfWeek),
        ),
      ),
      div(cls := "hours")(
        " *CLOSED* ",
      ),
    )

  def createBusTimeElement(
    restaurantWithStatus: RestaurantWithStatus,
    openForOrders: Boolean,
  ): JsDom.TypedTag[Div] = {
    val externalActions =
      renderExternalActions(
        restaurantWithStatus.restaurantWithSchedule.externalActions,
        openForOrders,
      )
    Bulma.collapsedCardWithHeader(
      div(cls := "restaurant-header")(
        div(cls := "restaurant-name")(
//          if (carryOutStatus == Open || deliveryStatus == Open)
//            div(location.name + "Open now!")
//          else
          div(
            restaurantWithStatus.restaurantWithSchedule.location.humanFriendlyName,
          ),
        ),
        div(cls := "restaurant-call")(
          externalActions.head, // Unsafe
        ),
      ),
      div(
        svgIcon("glyphicons-basic-221-chevron-down.svg"),
      ),
      div(cls := "restaurant-information")(
        restaurantWithStatus.restaurantWithSchedule.businessDetails
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
            case completelyUnstructedOperation: CompletelyUnstructedOperation =>
              div(cls := "completely-unstructured-operation")(
                completelyUnstructedOperation.instructions,
              )
          }
          .getOrElse(
            div(
              "Please visit these sites for more detailed information.",
            ),
          ),
      ),
      externalActions.tail,
    )(
      data("location") := restaurantWithStatus.restaurantWithSchedule.location.elementName,
    )
  }

  def activateModal(targetName: String): Unit =
    org.scalajs.dom.document.body
      .querySelector(targetName)
      .classList
      .add("is-active")

  def modalContentElementNameTyped(location: Name,
                                   routeName: RestaurantGroupName) =
    data("schedule-modal") := modalContentElementName(location,
                                                      routeName)

  def modalContentElementName(location: Name,
                              routeName: RestaurantGroupName) =
    "modal_content_" + routeName.name + "_" + location.elementName

  def renderExternalAction(externalAction: ExternalAction,
                           openForOrders: Boolean) =
    externalAction match {
      case VisitHomePage(website) =>
        renderWebsiteLink(website, openForOrders)
      case VisitFacebookPage(website) =>
        renderWebsiteLink(website, openForOrders)
      case CallLocation(phoneNumber) =>
        phoneButton(phoneNumber, openForOrders)
    }

  def structuredSetOfUpcomingArrivals(
    upcomingArrivalInfo: Seq[RestaurantWithStatus],
    routeName: Name,
  ) =
    div(
      div(cls := "route-header")(
        span(cls := "route-header_name")(
          routeName.humanFriendlyName,
        ),
      ),
      upcomingArrivalInfo.map {
        case restaurantWithStatus: RestaurantWithStatus => {
          val openForOrders =
            (restaurantWithStatus.carryOutStatus == Open || restaurantWithStatus.deliveryStatus == Open)
          TagsOnlyLocal.createBusTimeElement(
            restaurantWithStatus,
            openForOrders,
          )
        }
      },
    )

  def renderExternalActions(externalActions: ExternalActionCollection,
                            openForOrders: Boolean,
  ) =
    renderExternalAction(
      externalActions.primary,
      openForOrders,
    ) +: externalActions.others.map(
      externalAction =>
        renderExternalAction(externalAction, openForOrders),
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
