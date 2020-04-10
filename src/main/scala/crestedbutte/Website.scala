package crestedbutte

case class Website(url: String, name: String)

object Website {

  def facebookPage(url: String) =
    Website(url, "Facebook")

  def global(url: String) =
    Website(url, "Website")
}
