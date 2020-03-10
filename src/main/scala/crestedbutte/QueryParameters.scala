package crestedbutte

import crestedbutte.time.BusTime
import zio.ZIO

object QueryParameters {

  val getCurrentTimeParamValue =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          UrlParsing
            .getUrlParameter(
              browser.browser.window().location.toString,
              "time", // TODO Ugly string value
            )
            .map(BusTime(_)),
      )

  val getRouteQueryParamValue =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          UrlParsing
            .getUrlParameter(
              browser.browser.window().location.toString,
              "route", // TODO Ugly string value
            ),
      )

  val getCurrentPageMode =
    ZIO.environment[Browser].map { browser =>
      UrlParsing // Make the url/query param functions part of the Browser.
        .getUrlParameter(
          browser.browser.window().location.toString,
          "mode", // TODO Ugly string value
        )
        .flatMap(rawString => AppMode.fromString(rawString))
        .getOrElse(AppMode.Production)
    }
}
