package crestedbutte

import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.Node
import scalatags.JsDom
import zio.ZIO

object DomManipulation {
  import scalatags.JsDom.all._

  def createAndApplyPageStructure(
    pageMode: AppMode.Value
  ): ZIO[Browser, Nothing, Node] =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          browser.browser
            .body()
            .appendChild(
              TagsOnly.overallPageLayout(pageMode).render
            )
      )

  def appendMessageToPage(
    message: String
  ): ZIO[Browser, Throwable, Unit] =
    ZIO
      .environment[Browser]
      .map[Unit](
        browser =>
          browser.browser
            .querySelector("#activity-log")
            .foreach(_.appendChild(div(message).render))
      )

  def updateUpcomingBusSectionInsideElement(
    elementName: String,
    newContent: JsDom.TypedTag[Div],
    routeMode: RouteMode.Value
  ): ZIO[Browser, Nothing, Unit] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .querySelector(s"#$elementName") // TODO Handle case where this is missing
          .foreach { routeElementResult =>
            routeElementResult
              .querySelector("#upcoming-buses")
              .innerHTML = ""
            if (routeMode == RouteMode.Active) {
              routeElementResult
                .querySelector("#upcoming-buses")
                .appendChild(newContent.render)
            } else {
              println("Hiding instead of removing")
              routeElementResult.setAttribute("style",
                                              "visibility='hidden'")
            }
          }
      }

  def hideUpcomingBusSectionInsideElement(
    elementName: String
  ): ZIO[Browser, Nothing, Unit] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .querySelector(s"#$elementName") // TODO Handle case where this is missing
          .foreach { routeElementResult =>
            routeElementResult.setAttribute("style",
                                            "visibility='hidden'")
          }
      }

}
