package crestedbutte

import crestedbutte.Location.StopLocation
import crestedbutte.dom.Bulma
import crestedbutte.time.{BusDuration, BusTime}
import org.scalajs.dom.html.{Anchor, Div}
import scalatags.JsDom

object TagsOnly {
  import scalatags.JsDom.all._

  def createPopupContent(scheduleAtStop: RestaurantWithSchedule) =
    div(
      div(id := s"popup_${scheduleAtStop.location}",
          cls := "overlay light")(
        a(cls := "cancel", href := "#")("x" /*&times*/ ),
        div(cls := "popup")(
          h2("Later Arrivals"),
          div(cls := "content")(
            scheduleAtStop.times.map(
              time =>
                div(
                  span(time.toDumbAmericanString),
                ),
            ),
          ),
        ),
      ),
    )

  def hamburgerMenu() =
    a(role := "button",
      cls := "navbar-burger",
      aria.label := "menu",
      aria.expanded := "false")(
      span(aria.hidden := "true"),
      span(aria.hidden := "true"),
      span(aria.hidden := "true"),
    )

  def overallPageLayout(pageMode: AppMode.Value,
                        componentData: Seq[ComponentData]) =
    div(id := "container")(
      Bulma.menu(componentData),
      componentData.map(
        singleComponentData =>
          busScheduleDiv(singleComponentData.componentName),
      ),
      if (pageMode == AppMode.Development) {
        div(
          button(id := ElementNames.Notifications.requestPermission,
                 cls := "button")(
            "Request Notifications Permission",
          ),
          button(
            id := ElementNames.Notifications.submitMessageToServiceWorker,
            cls := "button",
          )(
            "SubmitMessage to SW",
          ),
        )
      } else div(),
    )

  def busScheduleDiv(containerName: String) =
    div(cls := ElementNames.BoxClass, id := containerName)(
      div(cls := "timezone"),
      div(id := ElementNames.contentName),
    )

  //  <a href="tel:123-456-7890">123-456-7890</a>
  def safeRideLink(
    safeRideRecommendation: CallToOrder,
  ): JsDom.TypedTag[Div] =
    div(cls := "late-night-call-button")(
      button(
        onclick :=
          s"window.location.href = 'tel:${safeRideRecommendation.phoneNumber}';",
        cls := "button",
      )(
        img(
          cls := "glyphicon",
          src := "/glyphicons/svg/individual-svg/glyphicons-basic-465-call.svg",
          alt := "Call Late Night Shuttle!",
        ),
        safeRideRecommendation.message,
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

  def createBusTimeElement(
    location: Location.Value,
    content: JsDom.TypedTag[Div],
    /* TODO: waitDuration: Duration*/
  ): JsDom.TypedTag[Div] =
    div(
      width := "100%",
      cls := "stop-information",
    )(
      div(cls := "map-link")(
        // TODO Re-enable once maps are more polished
        //  geoLinkForStop(location)
      ),
      div(cls := "stop-name")(
        div(location.name),
      ),
      div(cls := "stop-alt-name")(
        div(location.altName),
      ),
      div(cls := "upcoming-information")(
        content,
      ),
    )

  def geoLinkForStop(stopLocation: StopLocation) =
    a(
      cls := "link",
      //    <a href="geo:37.786971,-122.399677;u=35">open map</a>
//          href := s"geo:${stopLocation.gpsCoordinates.latitude}, ${stopLocation.gpsCoordinates.longitude}"
      href := s"https://www.google.com/maps/search/?api=1&query=${stopLocation.gpsCoordinates.latitude},${stopLocation.gpsCoordinates.longitude}",
    )(svgIcon("glyphicons-basic-592-map.svg"))

  def activateModal(targetName: String): Unit =
    org.scalajs.dom.document.body
      .querySelector(targetName)
      .classList
      .add("is-active")

  def modalContentElementNameTyped(location: Location.Value,
                                   routeName: RouteName) =
    data("schedule-modal") := modalContentElementName(location,
                                                      routeName)

  def modalContentElementName(location: Location.Value,
                              routeName: RouteName) =
    "modal_content_" + routeName.name + "_" + location.elementName

  def renderStopTimeInfo(stopTimeInfo: StopTimeInfo,
                         busScheduleAtStop: RestaurantWithSchedule,
                         routeName: RouteName) =
    div(
      button(
        cls := "arrival-time button open-arrival-time-modal",
        modalContentElementNameTyped(
          busScheduleAtStop.location,
          routeName,
        ),
//          data("schedule-modal") := modalContentElementName(
//            busScheduleAtStop.location,
//            routeName
//          ),
        onclick := {},
//          s"activateModal('#popup_${busScheduleAtStop.location}');",
        data("lossless-value") := stopTimeInfo.time.toString,
      )(stopTimeInfo.time.toDumbAmericanString),
      div(cls := "wait-time")(
        renderWaitTime(stopTimeInfo.waitingDuration),
        Bulma.bulmaModal(
          busScheduleAtStop,
          modalContentElementName(busScheduleAtStop.location,
                                  routeName),
        ),
      ),
    )

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
        case UpcomingArrivalInfoWithFullSchedule(
            UpcomingArrivalInfo(location, content),
            fullScheduleAtStop,
            ) => {
          TagsOnly.createBusTimeElement(
            location,
            content match {
              case Left(stopTimeInfo) =>
                renderStopTimeInfo(
                  stopTimeInfo,
                  fullScheduleAtStop,
                  upcomingArrivalComponentData.routeName,
                )
              case Right(safeRideRecommendation) =>
                safeRideLink(safeRideRecommendation)
            },
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
      alt := "Thanks for riding the bus!",
    )
  /*
  glyphicons-basic-591-map-marker.svg
  glyphicons-basic-417-globe.svg
  glyphicons-basic-262-direction-empty.svg
  glyphicons-basic-581-directions.svg
  glyphicons-basic-697-directions-sign.svg

 */

}
