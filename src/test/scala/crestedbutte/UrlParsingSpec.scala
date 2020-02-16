package crestedbutte

import zio.test.Assertion.equalTo
import zio.test.{DefaultRunnableSpec, assert, suite, test}

object UrlParsingSpec
  extends DefaultRunnableSpec(
    suite("HelloWorldSpec")(
      test("after last bus has run") {
        val fullUrl = "http://0.0.0.0:8000/index_dev.html?mode=DEV&homeRoute=TOWN_SHUTTLE"
        val queryParameters = UrlParsing.getUrlParameters(fullUrl)
        queryParameters.foreach(param=>println("Name: " + param._1 + " Value: " + param._2.head))
        assert(1, equalTo(1))
        assert(UrlParsing.getUrlParameter(fullUrl, "mode"), equalTo(Some("DEV")))

        assert(UrlParsing.getPath(fullUrl), equalTo("/index_dev.html"))
      }
    )
  )
