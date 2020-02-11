package crestedbutte

import crestedbutte.StopLocation.StopLocation
import org.scalajs.dom.html.{Anchor, Div}
import scalatags.JsDom

object TagsOnly {
  import scalatags.JsDom.all._

  val overallPageLayout =
    div(id := "container")(
      div(cls := "box", id := "upcoming-buses")(
        h3(cls := "upcoming-buses-title")(
          "Upcoming Buses"
        )
      ),
      div(cls := "box")(
        div("Future Work: Condo Loops")
      ),
      div(cls := "box")(
        div("Future Work: RTA buses!")
      )
    )

//  <a href="tel:123-456-7890">123-456-7890</a>
  def safeRideLink(
    safeRideRecommendation: SafeRideRecommendation
  ): JsDom.TypedTag[Anchor] =
    a(href := s"tel:${safeRideRecommendation.phoneNumber}",
      cls := "link")(
      safeRideRecommendation.message
    )

  def renderWaitTime(duration: BusDuration) =
    if (duration.toMinutes == 0)
      "Leaving!"
    else
      duration.toMinutes + " min."

  def createBusTimeElement(
    location: StopLocation.Value,
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
//          href := s"geo:${stopLocation.gpsCoordinates.latitude}, ${stopLocation.gpsCoordinates.longitude}"
        href := s"https://www.google.com/maps/search/?api=1&query=${stopLocation.gpsCoordinates.latitude},${stopLocation.gpsCoordinates.longitude}"
      )(stopLocation.name)
    )
//    <a href="geo:37.786971,-122.399677;u=35">open map</a>

  def structuredSetOfUpcomingArrivals(
    upcomingArrivalInfo: List[UpcomingArrivalInfo]
  ) =
    div(
      h4(textAlign := "center")("Upcoming Buses"),
      upcomingArrivalInfo.map {
        case UpcomingArrivalInfo(location, content) =>
          TagsOnly.createBusTimeElement(
            location,
            content match {
              case Left(stopTimeInfo) =>
                    div(
                    div(cls := "arrival-time")(stopTimeInfo.time.toString),
                    div(cls := "wait-time")(renderWaitTime(stopTimeInfo.waitingDuration))
                  )
              case Right(safeRideRecommendation) =>
                div(TagsOnly.safeRideLink(safeRideRecommendation))
            }
          )
      }
    )

}
