package crestedbutte

import scala.scalajs.js

object UrlParsing {

  def getPath(url: String) =
    java.net.URI
      .create(url)
      .getPath

  def getUrlParameters(url: String): Map[String, Array[String]] =
    if (url.contains('?'))
      java.net.URI
        .create(url)
        .getQuery
        .split('&')
        .map(js.URIUtils.decodeURIComponent)
        .map { p =>
          val split = p.split('=')
          (split.head, split.tail.mkString("="))
        }
        .groupBy(_._1)
        .map(m => m._1 -> m._2.map(_._2))
    else Map()

  def getUrlParameter(url: String,
                      parameter: String): Option[String] =
    getUrlParameters(url).get(parameter).flatMap(_.headOption)
}
