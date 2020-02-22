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
    newContent: JsDom.TypedTag[Div]
  ) =
    ZIO
      .environment[Browser]
      .map { browser =>
        println("Trying to get element: " + s"#${elementName}")
        val townShuttleElementResult = browser.browser
          .body()
          .querySelector(s"#${elementName}")
        townShuttleElementResult
          .querySelector("#upcoming-buses")
          .innerHTML = ""
        townShuttleElementResult
          .querySelector("#upcoming-buses")
          .appendChild(newContent.render)
        println("added content successfully")
      }

}
