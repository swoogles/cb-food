package crestedbutte

import java.time.format.DateTimeFormatter

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

  def createBusTimeElement(
    location: StopLocation.Value,
    content: Either[String, JsDom.TypedTag[Anchor]]
    /* TODO: waitDuration: Duration*/
  ): JsDom.TypedTag[Div] =
    div(
      width := "100%",
      style := "text-align:right; border-bottom: 1px solid white; padding-bottom: 5px;"
    )(
      span(width := "50%")(location.name),
      span(width := "50%", display := "inline-block;", style := "")(
        content match {
          case Left(contentString) => contentString
          case Right(phoneAnchor)  => phoneAnchor
        }
      )
    )

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
                  stopTimeInfo.time
                    .format(dateFormat) + " (" + stopTimeInfo.waitingDuration.toMinutes + " min)"
                )
              case Right(safeRideRecommendation) =>
                Right(TagsOnly.safeRideLink(safeRideRecommendation))
            }
          )
      }
    )

}
