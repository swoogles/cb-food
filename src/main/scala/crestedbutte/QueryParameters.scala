package crestedbutte

import zio.ZIO

object QueryParameters {

  def getRequired[T](
    parameterName: String,
    typer: String => T,
  ): ZIO[Browser, Nothing, Option[T]] =
    getOptional(parameterName, raw => Some(typer(raw)))

  def getOptional[T](parameterName: String,
                     typer: String => Option[T]) =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          UrlParsing
            .getUrlParameter(
              browser.browser.window().location.toString,
              parameterName,
            )
            .flatMap(typer),
      )

  def getOptionalZ[T](
    parameterName: String,
    typer: String => Option[T],
  ): ZIO[Browser, Unit, T] =
    ZIO
      .environment[Browser]
      .map { browser =>
        val result: Option[T] =
          UrlParsing
            .getUrlParameter(
              browser.browser.window().location.toString,
              parameterName,
            )
            .flatMap(typer)
        result
      }
      .flatMap(optResult => ZIO.fromOption(optResult))
}
