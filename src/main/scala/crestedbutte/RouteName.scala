package crestedbutte

object RouteName extends Enumeration {

  protected case class Val(name: String) extends super.Val(name)
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  // TODO Check ordering of all coordinates
  val TownLoop: Val = Val("Town_Loop")
  val ThreeSeasonsLoop: Val = Val("Three_Seasons_Loop")

}
