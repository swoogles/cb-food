package crestedbutte

import java.net.URI

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
            .querySelector(selectors),
        )
      if (result.isEmpty)
        println(
          "Failed to retrieve element with selector: " + selectors,
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
                                       paramValue),
        )

    override def alterUrlWithNewValue(url: String,
                                      paramName: String,
                                      paramValue: String): Unit =
      browser
        .window()
        .history
        .pushState(
          "stateData",
          "newTitle",
          UrlParsing.replaceParamInUrl(url, paramName, paramValue),
        )

    def workOnFullHtmlElement(function: (Element) => Unit) =
      function(
        org.scalajs.dom.document.querySelector("html"),
      )

    override def querySelectorAll(selectors: String): Seq[Node] =
      convertNodesToList(
        body()
          .querySelectorAll(selectors),
      )

    override def convertNodesToList(nodes: NodeList): Seq[Node] =
      for { i <- Range(0, nodes.length) } yield nodes(i)

    override def url(): URI =
      UrlParsing.parseRawUrl(window().location.toString)
  }

}
object BrowserLive extends BrowserLive
