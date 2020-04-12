package crestedbutte.dom

import crestedbutte.{ComponentData, RestaurantWithSchedule}
import crestedbutte.TagsOnlyLocal.svgIconForAlarm

object BulmaLocal {
  import scalatags.JsDom.all._

  def bulmaModal(scheduleAtStop: RestaurantWithSchedule,
                 idValue: String) =
    div(id := idValue, cls := "modal")(
      div(cls := "modal-background")(),
      div(cls := "modal-content",
          backgroundColor := "rgba(68, 68, 68, 1.0)",
          marginLeft := "45px",
          marginRight := "45px")(
        h4(textAlign := "center")(
          scheduleAtStop.location.name,
        ),
        h5(textAlign := "center")(
          "Upcoming Arrivals",
        ),
        scheduleAtStop.times.map(
          time =>
            div(textAlign := "center",
                verticalAlign := "middle",
                paddingBottom := "3px")(
              span(time.toDumbAmericanString),
              // TODO Re-enable once Notifications are more solid
//              svgIconForAlarm(
//                "glyphicons-basic-443-bell-ringing.svg",
//                "arrival-time-alarm",
//                time
//              )
            ),
        ),
      ),
      button(cls := "modal-close is-large", aria.label := "close")(),
    )
}
