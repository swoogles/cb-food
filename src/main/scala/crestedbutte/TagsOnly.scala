package crestedbutte

import crestedbutte.Location.StopLocation
import crestedbutte.dom.Bulma
import crestedbutte.time.{BusDuration, BusTime}
import org.scalajs.dom.html.{Anchor, Div}
import org.scalajs.dom.raw.MouseEvent
import scalatags.JsDom

object TagsOnly {
  import scalatags.JsDom.all._

  def createPopupContent(scheduleAtStop: BusScheduleAtStop) =
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
                  svgIconForAlarm(
                    "glyphicons-basic-443-bell-ringing.svg",
                    "arrival-time-alarm",
                    time
                  )
                )
            )
          )
        )
      )
    )

  def hamburgerMenu() =
    a(role := "button",
      cls := "navbar-burger",
      aria.label := "menu",
      aria.expanded := "false")(
      span(aria.hidden := "true"),
      span(aria.hidden := "true"),
      span(aria.hidden := "true")
    )

  def overallPageLayout(pageMode: AppMode.Value) =
    div(id := "container")(
      Bulma.menu(),
      div(cls := ElementNames.BoxClass,
          id := ElementNames.TownShuttles.containerName)(
        div(id := ElementNames.TownShuttles.contentName)
      ),
      if (pageMode == AppMode.Development) {
        button(id := ElementNames.Notifications.requestPermission,
               cls := "button")(
          "Request Notifications Permission"
        )
      } else div(),
      if (pageMode == AppMode.Development) {
        button(
          id := ElementNames.Notifications.submitMessageToServiceWorker,
          cls := "button"
        )(
          "SubmitMessage to SW"
        )
      } else div(),
      if (pageMode == AppMode.Development) {
        button(id := ElementNames.Notifications.notificationAction)(
          "10:20"
        )
      } else div(),
      div(cls := ElementNames.BoxClass,
          id := ElementNames.ThreeSeasonsLoop.containerName)(
        div(id := ElementNames.ThreeSeasonsLoop.contentName)(
          )
      )
//      div(cls := ElementNames.BoxClass)(
//        div("Future Work: RTA buses!")
//      )
    )

//  <a href="tel:123-456-7890">123-456-7890</a>
  def safeRideLink(
    safeRideRecommendation: LateNightRecommendation
  ): JsDom.TypedTag[Div] =
    div(cls := "late-night-call-button")(
      button(
        onclick :=
          s"window.location.href = 'tel:${safeRideRecommendation.phoneNumber}';",
        cls := "button"
      )(
        img(
          cls := "glyphicon",
          src := "/glyphicons/svg/individual-svg/glyphicons-basic-465-call.svg",
          alt := "Call Late Night Shuttle!"
        ),
        safeRideRecommendation.message
      )
    )

  def phoneLink(
    phoneNumber: PhoneNumber
  ): JsDom.TypedTag[Anchor] =
    a(href := s"tel:${phoneNumber.number}", cls := "link")(
      phoneNumber.name
    )

  def renderWaitTime(duration: BusDuration) =
    if (duration.toMinutes == 0)
      "Leaving!"
    else
      duration.toMinutes + " min."

  def createBusTimeElement(
    location: Location.Value,
    content: JsDom.TypedTag[Div]
    /* TODO: waitDuration: Duration*/
  ): JsDom.TypedTag[Div] =
    div(
      width := "100%",
      cls := "stop-information"
    )(
      div(cls := "map-link")(
        geoLinkForStop(location)
      ),
      div(cls := "stop-name")(
        div(location.name)
      ),
      div(cls := "stop-alt-name")(
        div(location.altName)
      ),
      div(cls := "upcoming-information")(
        content
      )
    )

  def geoLinkForStop(stopLocation: StopLocation) =
    a(
      cls := "link",
      //    <a href="geo:37.786971,-122.399677;u=35">open map</a>
//          href := s"geo:${stopLocation.gpsCoordinates.latitude}, ${stopLocation.gpsCoordinates.longitude}"
      href := s"https://www.google.com/maps/search/?api=1&query=${stopLocation.gpsCoordinates.latitude},${stopLocation.gpsCoordinates.longitude}"
    )(svgIcon("glyphicons-basic-591-map-marker.svg"))

  def activateModal(targetName: String): Unit =
    org.scalajs.dom.document.body
      .querySelector(targetName)
      .classList
      .add("is-active")

  def modalContentElementNameTyped(location: Location.Value,
                                   routeName: RouteName.Value) =
    data("schedule-modal") := modalContentElementName(location,
                                                      routeName)

  def modalContentElementName(location: Location.Value,
                              routeName: RouteName.Value) =
    "modal_content_" + routeName.humanReadibleName + "_" + location.elementName

  def renderStopTimeInfo(stopTimeInfo: StopTimeInfo,
                         busScheduleAtStop: BusScheduleAtStop,
                         routeName: RouteName.Value) =
    div(
      button(
        cls := "arrival-time button open-arrival-time-modal",
        modalContentElementNameTyped(
          busScheduleAtStop.location,
          routeName
        ),
//          data("schedule-modal") := modalContentElementName(
//            busScheduleAtStop.location,
//            routeName
//          ),
        onclick := {},
//          s"activateModal('#popup_${busScheduleAtStop.location}');",
        data("lossless-value") := stopTimeInfo.time.toString
      )(stopTimeInfo.time.toDumbAmericanString),
      div(cls := "wait-time")(
        renderWaitTime(stopTimeInfo.waitingDuration),
        Bulma.bulmaModal(
          busScheduleAtStop,
          modalContentElementName(busScheduleAtStop.location,
                                  routeName)
        )
      )
    )

  def structuredSetOfUpcomingArrivals(
    upcomingArrivalComponentData: UpcomingArrivalComponentData
  ) =
    div(
      div(cls := "route-header")(
        span(cls := "route-header_name")(
          "Upcoming " + upcomingArrivalComponentData.routeName.humanReadibleName + " Buses"
        ),
        img(
          cls := "glyphicon route-header_icon",
          src := "/glyphicons/svg/individual-svg/glyphicons-basic-32-bus.svg",
          alt := "Thanks for riding the bus!"
        )
      ),
      upcomingArrivalComponentData.upcomingArrivalInfo.map {
        case UpcomingArrivalInfoWithFullSchedule(
            UpcomingArrivalInfo(location, content),
            fullScheduleAtStop
            ) => {
          TagsOnly.createBusTimeElement(
            location,
            content match {
              case Left(stopTimeInfo) =>
                renderStopTimeInfo(
                  stopTimeInfo,
                  fullScheduleAtStop,
                  upcomingArrivalComponentData.routeName
                )
              case Right(safeRideRecommendation) =>
                safeRideLink(safeRideRecommendation)
            }
          )
        }
      }
    )

  def svgIconForAlarm(name: String,
                      classes: String,
                      busTime: BusTime) =
    img(
      cls := "glyphicon " + classes,
      src := s"/glyphicons/svg/individual-svg/$name",
      alt := "Thanks for riding the bus!",
      data("lossless-value") := busTime.toString,
      verticalAlign := "middle"
    )

  def svgIcon(name: String) =
    img(
      cls := "glyphicon",
      src := s"/glyphicons/svg/individual-svg/$name",
      alt := "Thanks for riding the bus!"
    )
  /*
  glyphicons-basic-591-map-marker.svg
  glyphicons-basic-417-globe.svg
  glyphicons-basic-262-direction-empty.svg
  glyphicons-basic-581-directions.svg
  glyphicons-basic-697-directions-sign.svg

 */

}
