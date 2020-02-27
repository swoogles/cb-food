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

  def menu() =
    div(cls := "navbar",
        role := "navigation",
        aria.label := "main navigation")(
      div(cls := "navbar-brand")(
        a(cls := "navbar-item", href := "https://bulma.io")(
          img(src := "https://bulma.io/images/bulma-logo.png",
              width := "112",
              height := "28")
        ),
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
          a(cls := "navbar-item")("Home"),
          a(cls := "navbar-item")("Documentation"),
          div(cls := "navbar-item has-dropdown is-hoverable")(
            a(cls := "navbar-link")("More"),
            div(cls := "navbar-dropdown")(
              a(cls := "navbar-item")("About"),
              a(cls := "navbar-item")("Jobs"),
              a(cls := "navbar-item")("Contact"),
              hr(cls := "navbar-divider"),
              a(cls := "navbar-item")("Report an issue")
            )
          )
        ),
        div(cls := "navbar-end")(
          div(cls := "navbar-item")(
            div(cls := "buttons")(
              a(cls := "button is-primary")(strong("Sign up")),
              a(cls := "button is-light")("Log in")
            )
          )
        )
      )
    )
}
