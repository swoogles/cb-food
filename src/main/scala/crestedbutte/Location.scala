package crestedbutte

case class Location(officialName: String) {

  val elementName: String =
    officialName
      .map(
        (letter: Char) =>
          if (letter.isLetter) letter.toString else "_",
      )
      .mkString
}
