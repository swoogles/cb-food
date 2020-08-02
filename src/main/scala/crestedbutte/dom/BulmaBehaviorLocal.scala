package crestedbutte.dom

import org.scalajs.dom.raw.MouseEvent
import zio.{IO, ZIO}
import zio.Runtime.default
import crestedbutte.Browser.Browser

object BulmaBehaviorLocal {

  def addMenuBehavior(input: IO[Nothing, Unit]) =
    ZIO
      .access[Browser](_.get)
      .map { browser =>
        browser
          .querySelector(
            "#main-menu",
          )
          .map { element =>
            browser
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
                    browser.rewriteCurrentUrl("route", targetRoute)
                    browser
                      .querySelector("#navbarBasicExample")
                      .foreach(_.classList.remove("is-active"))
                    default.unsafeRunAsync(input)(_ => ())
                  },
                )
              }

            element
          }
      }

}
