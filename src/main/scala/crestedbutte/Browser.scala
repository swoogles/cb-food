package crestedbutte

import org.scalajs.dom.Window
import org.scalajs.dom.raw.HTMLElement

object Browser {

  trait Service {
    def body(): HTMLElement
    def window(): Window
  }
}

trait Browser {
  def browser: Browser.Service
}
