package crestedbutte

import zio.ZIO

object DomMonitoring {

  val modalIsOpen: ZIO[Browser, Nothing, Boolean] =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .body()
          .querySelectorAll(".modal.is-active") // LONG SEARCH
          .length > 0
      }

}
