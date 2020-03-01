package crestedbutte

import zio.test.Assertion.equalTo
import zio.test.{assert, suite, test, DefaultRunnableSpec}

object UrlParsingSpec
    extends DefaultRunnableSpec(
      suite("UrlParsingSpec")(
        test("after last bus has run") {
          val fullUrl =
            "http://0.0.0.0:8000/index_dev.html?mode=DEV&homeRoute=TOWN_SHUTTLE"
          val queryParameters = UrlParsing.getUrlParameters(fullUrl)
          assert(1, equalTo(1))
          assert(UrlParsing.getUrlParameter(fullUrl, "mode"),
                 equalTo(Some("DEV")))

          assert(UrlParsing.getPath(fullUrl),
                 equalTo("/index_dev.html"))
        },

        test("Can replace a query Param in existing url") {
          val fullUrl =
            "http://0.0.0.0:8000/index_dev.html?mode=DEV&homeRoute=TOWN_SHUTTLE"
          val updatedUrl = UrlParsing.replaceParamInUrl(fullUrl, "mode", "RANDOM_MODE")
          assert(updatedUrl.contains("RANDOM_MODE"), equalTo(true))
        },
        test("Can add a new query Param if not already in the URL") {
          val fullUrl =
            "http://0.0.0.0:8000/index_dev.html?homeRoute=TOWN_SHUTTLE"
          val updatedUrl = UrlParsing.replaceParamInUrl(fullUrl, "mode", "RANDOM_MODE")
          println("UpdatedUrl: " + updatedUrl)
          assert(updatedUrl.contains("RANDOM_MODE"), equalTo(true))
        },
          test("Parse a hostless URL") {
          val fullUrl =
            "/index_dev.html?mode=DEV&homeRoute=TOWN_SHUTTLE"
          val updatedUrl = UrlParsing.replaceParamInUrl(fullUrl, "mode", "RANDOM_MODE")
          assert(updatedUrl.contains("RANDOM_MODE"), equalTo(true))
        }
      )
    )
