package crestedbutte

import crestedbutte.Location.StopLocation
import crestedbutte.time.BusDuration
import org.scalajs.dom.html.{Anchor, Div}
import scalatags.JsDom

object TagsOnly {
  import scalatags.JsDom.all._

  def createPopupContent(scheduleAtStop: BusScheduleAtStop) =
    div(
      div(id := s"popup_${scheduleAtStop.location}",
          cls := "overlay light")(
        div(cls := "popup")(
          h2("Info box"),
          a(cls := "cancel close", href := "#")("x" /*&times*/ ),
          div(cls := "content")(
            scheduleAtStop.times.map(
              time => p(time.toDumbAmericanString)
            )
          )
        )
      )
    )

  def overallPageLayout(pageMode: AppMode.Value) =
    div(id := "container")(
      div(cls := ElementNames.BoxClass,
          id := ElementNames.TownShuttles.containerName)(
        div(id := ElementNames.TownShuttles.contentName),
        h3(cls := "upcoming-buses-title")(
          "Upcoming Town Shuttles"
        )
      ),
      button(id := ElementNames.Notifications.requestPermission,
             cls := "button")(
        "Request Notifications Permission"
      ),
      if (pageMode == AppMode.Development) {
        button(id := ElementNames.Notifications.notificationAction)(
          "10:20"
        )
      } else {
        div()
      },
      div(cls := ElementNames.BoxClass,
          id := ElementNames.ThreeSeasonsLoop.containerName)(
        div(id := ElementNames.ThreeSeasonsLoop.contentName)(
          h3(cls := "upcoming-buses-title")(
            "Upcoming Town Shuttles"
          )
        )
      ),
      div(cls := ElementNames.BoxClass)(
        div("Future Work: RTA buses!")
      )
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
      div(cls := "stop-name")(
        div(location.name, geoLinkForStop(location))
      ),
      div(cls := "upcoming-information")(
        content
      )
    )

  def geoLinkForStop(stopLocation: StopLocation) =
    div(
      a(
        cls := "link",
        //    <a href="geo:37.786971,-122.399677;u=35">open map</a>
//          href := s"geo:${stopLocation.gpsCoordinates.latitude}, ${stopLocation.gpsCoordinates.longitude}"
        href := s"https://www.google.com/maps/search/?api=1&query=${stopLocation.gpsCoordinates.latitude},${stopLocation.gpsCoordinates.longitude}"
      )(svgIcon("glyphicons-basic-591-map-marker.svg"))
    )

  def renderStopTimeInfo(stopTimeInfo: StopTimeInfo,
                         busScheduleAtStop: BusScheduleAtStop) =
    div(
      button(
        cls := "arrival-time button",
        onclick :=
          s"window.location.href = '#popup_${busScheduleAtStop.location}';",
        data("lossless-value") := stopTimeInfo.time.toString
      )(stopTimeInfo.time.toDumbAmericanString),
      div(cls := "wait-time")(
        renderWaitTime(stopTimeInfo.waitingDuration),
        createPopupContent(busScheduleAtStop)
      )
    )

  def structuredSetOfUpcomingArrivals(
    upcomingArrivalInfo: Seq[UpcomingArrivalInfoWithFullSchedule], // This should take one item
    routeName: String
  ) =
    div(
      div(cls := "route-header")(
        span(cls := "route-header_name")(
          "Upcoming " + routeName + " Buses"
        ),
        img(
          cls := "glyphicon route-header_icon",
          src := "/glyphicons/svg/individual-svg/glyphicons-basic-32-bus.svg",
          alt := "Thanks for riding the bus!"
        )
      ),
      upcomingArrivalInfo.map {
        case UpcomingArrivalInfoWithFullSchedule(
            UpcomingArrivalInfo(location, content),
            fullScheduleAtStop
            ) => {
          TagsOnly.createBusTimeElement(
            location,
            content match {
              case Left(stopTimeInfo) =>
                renderStopTimeInfo(stopTimeInfo, fullScheduleAtStop)
              case Right(safeRideRecommendation) =>
                safeRideLink(safeRideRecommendation)
            }
          )
        }
      }
    )

  def svgIcon(name: String) =
    img(
      cls := "glyphicon route-header_icon",
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
