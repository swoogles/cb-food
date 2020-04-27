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
            browser.browser
              .convertNodesToList(
                element.querySelectorAll(".navbar-item"),
              )
              .foreach { node =>
                node.addEventListener(
                  "click",
                  (_: MouseEvent) => {
                    println("Clicked a menu item")
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

  val isVisible = (element: Element) =>
    element.classList.contains("is-active")

}
