package crestedbutte

import java.time.{Duration, LocalTime}
import java.time.format.DateTimeFormatter

import crestedbutte.StopLocation.StopLocation
import org.scalajs.dom.html.{Anchor, Div}
import scalatags.JsDom

object TagsOnly {
  import scalatags.JsDom.all._

  val overallPageLayout =
    div(id := "container")(
      div(cls := "wrapper")(
        div(cls := "box c", id := "upcoming-buses")(
          h3(style := "text-align: center")(
            "Upcoming Buses"
          )
        ),
        div(cls := "box d")(
          div("Future Work: RTA buses")
        )
      )
    )

//  <a href="tel:123-456-7890">123-456-7890</a>
  def safeRideLink(
    safeRideRecommendation: SafeRideRecommendation
  ): JsDom.TypedTag[Anchor] =
    a(href := s"tel:${safeRideRecommendation.phoneNumber}")(
      safeRideRecommendation.message
    )

  val dateFormat = DateTimeFormatter.ofPattern("h:mm")

//  def renderWaitTime(duration: Duration) =

  def renderContent(
    content: Either[(LocalTime, Duration), JsDom.TypedTag[Anchor]]
  ) =
    content match {
      case Left((arrivalTime, waitTime)) =>
        div(
          div(cls := "arrival-time")(arrivalTime.format(dateFormat)),
          div(cls := "wait-time")(waitTime.toMinutes + " min.")
        )
      case Right(phoneAnchor) => div(phoneAnchor)
    }

  def createBusTimeElement(
    location: StopLocation.Value,
    content: Either[(LocalTime, Duration), JsDom.TypedTag[Anchor]]
    /* TODO: waitDuration: Duration*/
  ): JsDom.TypedTag[Div] =
    div(
      width := "100%",
      cls := "stop-information",
      style := "border-bottom: 1px solid white; padding-bottom: 5px;"
    )(
      div(cls := "stop-name")(geoLinkForStop(location)),
      div(cls := "upcoming-information",
          style := "text-align:left; ")(
        renderContent(content)
      )
    )

  def geoLinkForStop(stopLocation: StopLocation) =
    div(cls := "light-background")(
      a(
        href := s"geo:${stopLocation.gpsCoordinates.latitude}, ${stopLocation.gpsCoordinates.longitude}"
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
                val dateFormat = DateTimeFormatter.ofPattern("h:mm")
                Left(
                  (stopTimeInfo.time, stopTimeInfo.waitingDuration)
                )
              case Right(safeRideRecommendation) =>
                Right(TagsOnly.safeRideLink(safeRideRecommendation))
            }
          )
      }
    )

}
