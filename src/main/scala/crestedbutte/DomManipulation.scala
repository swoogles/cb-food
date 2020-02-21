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

  def updateUpcomingBusesSection(
    newContent: JsDom.TypedTag[Div]
  ): ZIO[Browser, Nothing, Unit] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .body()
          .querySelector("#upcoming-buses")
          .innerHTML = ""
        browser.browser
          .body()
          .querySelector("#upcoming-buses")
          .appendChild(newContent.render)
      }
}
