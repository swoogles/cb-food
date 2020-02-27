package crestedbutte

object RouteMode extends Enumeration {

  protected case class Val(name: String) extends super.Val(name)
  import scala.language.implicitConversions

  implicit def valueToVal(x: Value): Val =
    x.asInstanceOf[Val]

  type RouteMode = Value

  // TODO Check ordering of all coordinates
  val Active: Val = Val("Active")
  val Hidden: Val = Val("Hidden")

}
