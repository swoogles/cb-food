package crestedbutte

case class RouteName(userFriendlyName: String) {

  val name: String =
    userFriendlyName
      .map(
        (letter: Char) =>
          if (letter.isLetter) letter.toString else "_",
      )
      .mkString

  def elementNameMatches(elementName: String) =
    name == elementName
  // TODO Check ordering of all coordinates
  /*
  val TownLoop: Val = Val()

  val ThreeSeasonsLoop: Val =
    Val()

 */

}
