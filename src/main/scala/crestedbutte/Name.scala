package crestedbutte

case class Name(humanFriendlyName: String) {

  val elementName: String =
    humanFriendlyName
      .map(
        (letter: Char) =>
          if (letter.isLetter) letter.toString else "_",
      )
      .mkString

  def elementNameMatches(elementName: String) =
    this.elementName == elementName
}
