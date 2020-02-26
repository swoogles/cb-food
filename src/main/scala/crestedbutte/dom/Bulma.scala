package crestedbutte.dom

import crestedbutte.BusScheduleAtStop
import crestedbutte.Location.StopLocation
import crestedbutte.TagsOnly.svgIconForAlarm
import crestedbutte.time.{BusDuration, BusTime}
import org.scalajs.dom.html.{Anchor, Div}
import org.scalajs.dom.raw.MouseEvent
import scalatags.JsDom

object Bulma {
  import scalatags.JsDom.all._

  def bulmaModal(scheduleAtStop: BusScheduleAtStop, idValue: String) =
    div(id := idValue, cls := "modal")(
      div(cls := "modal-background")(),
      div(cls := "modal-content",
          backgroundColor := "rgba(68, 68, 68, 1.0)",
          marginLeft := "45px",
          marginRight := "45px")(
        h4(textAlign := "center")(
          scheduleAtStop.location.name
        ),
        h5(textAlign := "center")(
          "Upcoming Arrivals"
        ),
        scheduleAtStop.times.map(
          time =>
            div(textAlign := "center",
                verticalAlign := "middle",
                paddingBottom := "3px")(
              span(time.toDumbAmericanString),
              svgIconForAlarm(
                "glyphicons-basic-443-bell-ringing.svg",
                "arrival-time-alarm",
                time
              )
            )
        )
      ),
      button(cls := "modal-close is-large", aria.label := "close")()
    )

}
