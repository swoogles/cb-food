package crestedbutte.dom

import crestedbutte.Browser
import org.scalajs.dom.{Element, Event}
import org.scalajs.dom.raw.MouseEvent
import zio.{DefaultRuntime, IO, ZIO}

object BulmaBehaviorLocal {

  def addMenuBehavior(input: IO[Nothing, Unit]) =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .querySelector(
            "#main-menu",
          )
          .map { element =>
            new DefaultRuntime {}
              .unsafeRun(hideOnClickOutside(element).provide(browser))

            browser.browser
              .convertNodesToList(
                element.querySelectorAll(".navbar-item .route"),
              )
              .foreach { node =>
                node.addEventListener(
                  "click",
                  (_: MouseEvent) => {
                    val targetRoute =
                      node.attributes.getNamedItem("data-route").value
                    if (browser.browser
                          .url()
                          .getPath
                          .contains("index_dev"))
                      browser.browser.rewriteCurrentUrl("route",
                                                        targetRoute)
                    else
                      browser.browser
                        .alterUrlWithNewValue("/index.html",
                                              "route",
                                              targetRoute)
                    browser.browser
                      .querySelector("#navbarBasicExample")
                      .foreach(_.classList.remove("is-active"))
                    new DefaultRuntime {}.unsafeRun(input)
                  },
                )
              }

            element
          }
      }

  // This isn't really Bulma specific, rather than the .is-active class
  def hideOnClickOutside(element: Element) =
    ZIO
      .environment[Browser]
      .map { browser =>
        println("setting up click-outside-menu behavior")
        def outsideClickListener(): MouseEvent => Unit =
          (event: MouseEvent) => {
            println(
              "you might have clicked outside of the main-menu!",
            )
            // TODO Get rid of terrible cast! It probably doesn't even work anyways!
//            if (!element.contains(*clickedElement*) && isVisible(
            if (isVisible(
                  element,
                )) {
              println(
                "Just going to close the menu because you clicked on *anything*",
              )
              element.classList.remove("is-active")
              removeClickListener() // TODO Make unsafe behavior more explicit
            }
          }

        def removeClickListener() =
          () =>
            browser.browser
              .window()
              .document
              .removeEventListener("click", outsideClickListener())

        browser.browser
          .window()
          .document
          .addEventListener("click", outsideClickListener())
      }

  val isVisible = (element: Element) =>
    element.classList.contains("is-active")

}
