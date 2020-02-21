package crestedbutte

import crestedbutte.Location.StopLocation
import crestedbutte.time.BusDuration
import org.scalajs.dom.html.{Anchor, Div}
import scalatags.JsDom

object TagsOnly {
  import scalatags.JsDom.all._

  def overallPageLayout(pageMode: AppMode.Value) =
    div(id := "container")(
      div(cls := ElementNames.BoxClass,
          id := ElementNames.TownShuttles.containerName)(
        h3(cls := "upcoming-buses-title")(
          "Upcoming Town Shuttles"
        )
      ),
      button(id := ElementNames.Notifications.requestPermission)(
        "Request Notifications Permission"
      ),
      if (pageMode == AppMode.Development) {
        button(id := ElementNames.Notifications.notificationAction)(
          "10:20"
        )
      } else {
        div()
      },
      div(cls := ElementNames.BoxClass)(
        div("Future Work: Condo Loops")
      ),
      div(cls := ElementNames.BoxClass)(
        div("Future Work: RTA buses!")
      )
    )

//  <a href="tel:123-456-7890">123-456-7890</a>
  def safeRideLink(
    safeRideRecommendation: SafeRideRecommendation
  ): JsDom.TypedTag[Div] =
    div(
      phoneLink(
        PhoneNumber(safeRideRecommendation.phoneNumber,
                    safeRideRecommendation.message)
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
      div(cls := "stop-name")(geoLinkForStop(location)),
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
      )(stopLocation.name)
    )

  def renderStopTimeInfo(stopTimeInfo: StopTimeInfo) =
    div(
      div(
        cls := "arrival-time",
        data("lossless-value") := stopTimeInfo.time.toString
      )(stopTimeInfo.time.toDumbAmericanString),
      div(cls := "wait-time")(
        renderWaitTime(stopTimeInfo.waitingDuration)
      )
    )

  def structuredSetOfUpcomingArrivals(
    upcomingArrivalInfo: Seq[UpcomingArrivalInfo]
  ) =
    div(
      h4(textAlign := "center")("Upcoming Town Shuttles"),
      upcomingArrivalInfo.map {
        case UpcomingArrivalInfo(location, content) =>
          TagsOnly.createBusTimeElement(
            location,
            content match {
              case Left(stopTimeInfo) =>
                renderStopTimeInfo(stopTimeInfo)
              case Right(safeRideRecommendation) =>
                safeRideLink(safeRideRecommendation)
            }
          )
      }
    )

}
