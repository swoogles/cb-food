package crestedbutte.dom

import crestedbutte.{
  Browser,
  BrowserLive,
  ElementNames,
  ModalBehavior
}
import org.scalajs.dom.raw.MouseEvent
import zio.{DefaultRuntime, IO, ZIO}

object BulmaBehavior {

  def addMenuBehavior(input: IO[Nothing, Unit]) =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .querySelector(
            "#main-menu"
          )
          .map { element =>
            browser.browser
              .convertNodesToList(
                element.querySelectorAll(".navbar-item .route")
              )
              .foreach { node =>
                println(
                  "Found a menu node to attach behavior to: " + node.textContent
                )
                node.addEventListener(
                  "click",
                  (_: MouseEvent) => {
                    val targetRoute =
                      node.attributes.getNamedItem("data-route").value
                    println("targetRoute: " + targetRoute)
                    if (browser.browser
                          .url()
                          .getPath
                          .contains("index_dev")) {
                      println("Should rewrite index_dev route!")
                      browser.browser.rewriteCurrentUrl("route",
                                                        targetRoute)
                    } else {
                      println("Should rewrite index route!")
                      browser.browser
                        .alterUrlWithNewValue("/index.html",
                                              "route",
                                              targetRoute)
                    }
                    new DefaultRuntime {}.unsafeRun(input)
                  }
                )
              }

          }
      }
}
