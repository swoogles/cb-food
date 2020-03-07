package crestedbutte

object RouteName extends Enumeration {

  protected case class Val(userFriendlyName: String)
      extends super.Val(userFriendlyName) {

    val name: String =
      userFriendlyName
        .map(
          (letter: Char) =>
            if (letter.isLetter) letter.toString else "_"
        )
        .mkString
  }
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  // TODO Check ordering of all coordinates
  val TownLoop: Val = Val("Town Loop")

  val ThreeSeasonsLoop: Val =
    Val("Three Seasons Loop")

  val CrystalCastle: Val =
    Val("Crystal/Castle")

  val RtaNorthbound: Val =
    Val("Rta Northbound")

  val ColumbineLoop: Val =
    Val("Columbine Loop")

  val SnodgrassShuttle: Val =
    Val("Snodgrass Shuttle")

  def fromString(input: String) =
    values.find(_.name == input)
}
