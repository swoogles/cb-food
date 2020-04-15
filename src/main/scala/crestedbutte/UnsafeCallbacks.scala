package crestedbutte

import org.scalajs.dom.Node
import org.scalajs.dom.raw.{HTMLElement, MouseEvent}
import zio.ZIO

object UnsafeCallbacks {

  val attachCardClickBehavior =
    ZIO
      .environment[Browser]
      .map {
        // println("About to attach card behavior") // um, why does this code work if I restore this?
        browser =>
          browser.browser
            .querySelectorAll(".card")
            .collect{ case x: HTMLElement => x} // TODO Move this into browser interface. I want it basically everywhere.
            .foreach { modalOpenButton: HTMLElement =>
              println("Found a card. Attaching click behavior.")
              modalOpenButton
                .addEventListener(
                  "click",
                  (clickEvent: MouseEvent) => {
                    println("clicked a card.")

                    modalOpenButton
                          .querySelector(".pickup-schedule")
                          .classList
                          .remove("is-hidden")
                    modalOpenButton
                          .querySelector(".delivery-schedule")
                          .classList
                          .remove("is-hidden")
                  },
                )
            }
      }

  val attachMenuBehavior =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          browser.browser
            .window()
            .document
            .addEventListener(
              "DOMContentLoaded",
              (_: Any) => {

                def menuCallbackBehavior(node: Node,
                                         browser: Browser) =
                  (_: MouseEvent) =>
                    // Get the target from the "data-target" attribute
                    // POTENTIALLY VERY EXPENSIVE. It's jumping back to the root of the document with this search.
                    browser.browser
                      .querySelector(
                        "#" + node.attributes
                          .getNamedItem("data-target")
                          .value,
                      )
                      .map(_.classList.toggle("is-active"))

                browser.browser
                  .querySelectorAll(".navbar-burger")
                  .foreach(
                    node =>
                      node
                        .addEventListener(
                          "click",
                          menuCallbackBehavior(node, browser),
                        ),
                  )

              },
            ),
      )

}
