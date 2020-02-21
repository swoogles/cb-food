package crestedbutte

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, Window}

trait BrowserLive extends Browser {

  def browser: Browser.Service = new Browser.Service {
    override def body(): HTMLElement = document.body

    override def window(): Window = org.scalajs.dom.window
  }

}

object BrowserLive extends BrowserLive
