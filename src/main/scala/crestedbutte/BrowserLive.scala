package crestedbutte

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, Element, Node, NodeList, Window}

trait BrowserLive extends Browser {

  def browser: Browser.Service = new Browser.Service {
    override def body(): HTMLElement = document.body

    override def window(): Window = org.scalajs.dom.window

    override def querySelector(selectors: String): Option[Element] = {
      val result =
        Option(
          body()
            .querySelector(selectors)
        )
      if (result.isEmpty)
        println(
          "Failed to retrieve element with selector: " + selectors
        )
      result
    }

    def rewriteCurrentUrl(paramName: String, paramValue: String) =
      browser
        .window()
        .history
        .pushState(
          "stateData",
          "newTitle",
          UrlParsing.replaceParamInUrl(window().location.toString,
                                       paramName,
                                       paramValue)
        )

    def workOnFullHtmlElement(function: (Element) => Unit) =
      function(
        org.scalajs.dom.document.querySelector("html")
      )

    override def querySelectorAll(selectors: String): Seq[Node] = {
      val nodes =
        body()
          .querySelectorAll(selectors)

      for { i <- Range(0, nodes.length) } yield nodes(i)
    }

  }

}
object BrowserLive extends BrowserLive
