package crestedbutte

import org.scalajs.dom.Node
import org.scalajs.dom.raw.{HTMLElement, MouseEvent}
import zio.ZIO

object UnsafeCallbacks {

  val attachCardClickBehavior =
    ZIO
      .environment[Browser]
      .map {
        println("About to attach card behavior")
        browser =>
          browser.browser
            .querySelectorAll(".card")
            .foreach { modalOpenButton =>
              println("Found a card. Attaching click behavior.")
              modalOpenButton
                .addEventListener(
                  "click",
                  (clickEvent: MouseEvent) => {
                    println("clicked a card.")

                    modalOpenButton match {
                      case clickedElement: HTMLElement =>
                        clickedElement
                          .querySelector(".pickup-schedule")
                          .classList
                          .remove("is-hidden")
                        clickedElement
                          .querySelector(".delivery-schedule")
                          .classList
                          .remove("is-hidden")
                      case _ =>
                    }
//                    val modalContentId =
//                      modalOpenButton.attributes
//                        .getNamedItem("data-schedule-modal")
//                        .value
//
//                    clickEvent.preventDefault();

                    /*
              browser.browser
                .querySelector(
                  id(modalContentId),
                )
                .map {
                  modal =>
                    browser.browser
                      .workOnFullHtmlElement(
                        _.classList.add("is-clipped"),
                      )

                    modal
                      .querySelector(".modal-background")
                      .addEventListener(
                        "click",
                        (e: MouseEvent) => {
                          e.preventDefault();
                          removeClippedHtml(browser)
                          modal.classList.remove("is-active");
                        },
                      )

                    activateModal(
                      id(modalContentId),
                    )
                }

                   */
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
