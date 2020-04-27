package crestedbutte

import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.Node
import scalatags.JsDom
import zio.ZIO

object DomManipulation {
  import scalatags.JsDom.all._

  def createAndApplyPageStructure(
    pageMode: AppMode.Value,
    componentData: Seq[ComponentData],
  ): ZIO[Browser, Nothing, Node] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .querySelector("#landing-message")
          .map(browser.browser.body().removeChild(_))
        browser.browser
          .body()
          .appendChild(
            TagsOnlyLocal
              .overallPageLayout(pageMode, componentData)
              .render,
          )
      }

  def appendMessageToPage(
    message: String,
  ): ZIO[Browser, Throwable, Unit] =
    ZIO
      .environment[Browser]
      .map[Unit](
        browser => {
          println("Should show timezones: " + message)
          browser.browser
            .querySelector(".timezone")
            .foreach(_.appendChild(div(message).render))
        },
      )

  def updateUpcomingBusSectionInsideElement(
    elementName: String,
    newContent: JsDom.TypedTag[Div],
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

            routeElementResult.setAttribute("style", "display:box") // TODO or grid?

            routeElementResult
              .querySelector("#upcoming-buses")
              .appendChild(newContent.render)
          }
      }

  def hideElement(
    elementName: String,
  ): ZIO[Browser, Nothing, Unit] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .querySelector(s"#$elementName") // TODO Handle case where this is missing
          .foreach { routeElementResult =>
            routeElementResult.setAttribute(
              "style",
              "display:none",
            ) // Come up with way of hiding and collapsing
//            routeElementResult.innerHTML = ""
          }
      }

}
