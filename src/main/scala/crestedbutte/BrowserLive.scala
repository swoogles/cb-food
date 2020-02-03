package crestedbutte

import org.scalajs.dom.document

trait BrowserLive extends Browser {

  def dom: Browser.Service =
    () => document.body
}

object BrowserLive extends BrowserLive
