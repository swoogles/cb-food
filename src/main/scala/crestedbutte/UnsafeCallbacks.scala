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
//          browser.browser
//            .querySelectorAll(".card")
//            .collect { case x: HTMLElement => x } // TODO Move this into browser interface. I want it basically everywhere.
//            .foreach { modalOpenButton: HTMLElement =>
//              println("Found a card. Attaching click behavior.")
//              modalOpenButton
//                .addEventListener(
//                  "click",
//                  toggleSection(modalOpenButton),
//                )
//            }
          browser.browser
            .querySelectorAll(".restaurant-name")
            .collect { case x: HTMLElement => x } // TODO Move this into browser interface. I want it basically everywhere.
            .foreach { modalOpenButton: HTMLElement =>
              println("Found a card. Attaching click behavior.")
              modalOpenButton
                .addEventListener(
                  "click",
                  toggleSiblingSection(modalOpenButton),
                )
            }
          browser.browser
            .querySelectorAll(".card-header-icon")
            .collect { case x: HTMLElement => x } // TODO Move this into browser interface. I want it basically everywhere.
            .foreach { modalOpenButton: HTMLElement =>
              println("Found a card. Attaching click behavior.")
              modalOpenButton
                .addEventListener(
                  "click",
                  toggleSiblingSection2LevelsUp(modalOpenButton),
                )
            }
      }

  def toggleSiblingSection(
    htmlElement: HTMLElement,
  ): MouseEvent => Unit =
    (clickEvent: MouseEvent) => {
      val parentCard =
        htmlElement.parentElement.parentElement.parentElement.parentElement
      if (parentCard.querySelectorAll(".is-hidden").length > 0) {
        expandSection(parentCard)
      } else {
        collapseSection(parentCard)
      }
    }

  def toggleSiblingSection2LevelsUp(
    htmlElement: HTMLElement,
  ): MouseEvent => Unit =
    (clickEvent: MouseEvent) => {
      val parentCard =
        htmlElement.parentElement.parentElement
      if (parentCard.querySelectorAll(".is-hidden").length > 0) {
        expandSection(parentCard)
      } else {
        collapseSection(parentCard)
      }
    }

  def toggleSection(htmlElement: HTMLElement): MouseEvent => Unit =
    (clickEvent: MouseEvent) => {
      if (htmlElement.querySelectorAll(".is-hidden").length > 0) {
        expandSection(htmlElement)
      } else {
        collapseSection(htmlElement)
      }
    }

  def expandSection(htmlElement: HTMLElement): Unit = {
    println("clicked a card.")

    htmlElement
      .querySelector(".card-content")
      .classList
      .remove("is-hidden")
    htmlElement
      .querySelector(".card-footer")
      .classList
      .remove("is-hidden")

//      htmlElement.scrollIntoView(true)
  }

  def collapseSection(htmlElement: HTMLElement): Unit = {
    htmlElement
      .querySelector(".card-content")
      .classList
      .add("is-hidden")
    htmlElement
      .querySelector(".card-footer")
      .classList
      .add("is-hidden")

//      htmlElement.scrollIntoView(true)
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
