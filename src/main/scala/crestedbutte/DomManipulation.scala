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
            .body()
            .querySelector("#activity-log")
            .appendChild(div(message).render)
      )

  def updateUpcomingBusSectionInsideElement(
    elementName: String,
    newContent: JsDom.TypedTag[Div],
    routeMode: RouteMode.Value
  ): ZIO[Browser, Nothing, Unit] =
    ZIO
      .environment[Browser]
      .map { browser =>
        println("Trying to get element: " + s"#${elementName}")
        val routeElementResult =
          browser.browser
            .body()
            .querySelector(s"#${elementName}") // TODO Handle case where this is missing

        if (routeElementResult != null) {
          routeElementResult
            .querySelector("#upcoming-buses")
            .innerHTML = ""
          if (routeMode == RouteMode.Active) {
            routeElementResult
              .querySelector("#upcoming-buses")
              .appendChild(newContent.render)
          } else {
            routeElementResult.parentNode.removeChild(
              routeElementResult
            )
            println("leaving unwanted route empty")

          }
        }
        println("added content successfully")
      }

}
