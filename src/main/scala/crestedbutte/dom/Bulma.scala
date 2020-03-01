package crestedbutte.dom

import crestedbutte.BusScheduleAtStop
import crestedbutte.TagsOnly.svgIconForAlarm

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

  def menu() =
    div(id := "main-menu",
        cls := "navbar",
        role := "navigation",
        aria.label := "main navigation")(
      div(cls := "navbar-brand")(
        a(role := "button",
          cls := "navbar-burger burger",
          aria.label := "menu",
          aria.expanded := "false",
          data("target") := "navbarBasicExample")(
          span(aria.hidden := "true"),
          span(aria.hidden := "true"),
          span(aria.hidden := "true")
        )
      ),
      div(id := "navbarBasicExample", cls := "navbar-menu")(
        div(cls := "navbar-start")(
//          a(cls := "navbar-item")("Home"),
          div(cls := "navbar-item has-dropdown is-hoverable")(
            a(cls := "navbar-link")("Routes"),
            div(cls := "navbar-dropdown")(
              a(
                cls := "navbar-item route",
                data("route") := "Town_Loop"
//                href := "/index.html?route=Town_Loop"
              )("Town Loop"),
              a(
                cls := "navbar-item route",
                data("route") := "Three_Seasons_Loop"
//                href := "/index.html?route=Three_Seasons_Loop"
              )("Three Seasons Loop")
            )
          )
        ),
        div(cls := "navbar-end")(
//          div(cls := "navbar-item")(
//            div(cls := "buttons")(
//              a(cls := "button is-primary")(strong("Sign up")),
//              a(cls := "button is-light")("Log in")
//            )
//          )
        )
      )
    )
}
