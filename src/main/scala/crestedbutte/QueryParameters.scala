package crestedbutte

import zio.ZIO

object QueryParameters {

  // This String result should be immediately transformed into a stronger type
  def getBasicStringParam(parameterName: String) =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          UrlParsing
            .getUrlParameter(
              browser.browser.window().location.toString,
              parameterName,
            ),
      )

  def get[T](parameterName: String, typer: String => T) =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          UrlParsing
            .getUrlParameter(
              browser.browser.window().location.toString,
              parameterName,
            )
            .map(typer),
      )

}
