package crestedbutte

object RouteName extends Enumeration {

  protected case class Val(name: String, humanReadibleName: String)
      extends super.Val(name)
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  // TODO Check ordering of all coordinates
  val TownLoop: Val = Val("Town_Loop", "Town Loop")

  val ThreeSeasonsLoop: Val =
    Val("Three_Seasons_Loop", "Three Seasons Loop")

  def fromString(input: String) =
    values.find(_.name == input)
}