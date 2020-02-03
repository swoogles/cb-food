package crestedbutte

import org.scalajs.dom.raw.HTMLElement

object Browser {

  trait Service {
    def body(): HTMLElement
  }
}

trait Browser {
  def dom: Browser.Service
}