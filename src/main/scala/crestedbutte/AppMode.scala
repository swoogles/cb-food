package crestedbutte

object AppMode extends Enumeration {

  protected case class Val(name: String) extends super.Val(name)

  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  val Production: Val = Val("production")
  val Development: Val = Val("dev")

  def fromString(input: String) =
    values.find(_.name == input)
}
