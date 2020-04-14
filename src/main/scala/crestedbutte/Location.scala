package crestedbutte

object Location extends Enumeration {

  protected case class Val(name: String, altName: String = "")
      extends super.Val(name) {

    val elementName: String =
      name
        .map(
          (letter: Char) =>
            if (letter.isLetter) letter.toString else "_",
        )
        .mkString
  }
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  // TODO Check ordering of all coordinates
  val SecretStash: Val =
    Val("Secret Stash")

  val Clarks: Val = Val("6th/Belleview", "(Clarks Grocery)") //

  val Pitas: Val = Val("Pitas in Paradise")

  val Slogar: Val = Val("Slogar")

  val McGills: Val = Val("McGills")

  val BrickOven: Val = Val("The Brick Oven")

  val Bonez: Val = Val("Bonez")

  val Dogwood: Val = Val("Dogwood")

  val TinCupPasty: Val = Val("Tin Cup Pasty Co")

  val CoalCreekGrill: Val = Val("Coal Creek Grill")

  val TacosLocale: Val = Val("Tacos Locale")

  val Tullys: Val = Val("Tully's")

  val Montanyas: Val = Val("Montanya")

}
