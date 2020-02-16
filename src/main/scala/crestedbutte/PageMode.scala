package crestedbutte

object PageMode extends Enumeration {

  protected case class Val(name: String) extends super.Val(name)

  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  val Production: Val = Val("Production")
  val Development: Val = Val("Development")

  def fromString(input: String) =
    values.find(_.name == input)
}
