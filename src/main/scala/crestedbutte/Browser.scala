package crestedbutte

import org.scalajs.dom.{Element, Node, NodeList, Window}
import org.scalajs.dom.raw.HTMLElement

object Browser {

  trait Service {
    def body(): HTMLElement
    def window(): Window
    def querySelector(selectors: String): Option[Element]
    def querySelectorAll(selectors: String): Seq[Node]
    def workOnFullHtmlElement(function: (Element) => Unit)
  }
}

trait Browser {
  def browser: Browser.Service
}
