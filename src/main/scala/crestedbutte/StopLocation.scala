package crestedbutte

object StopLocation extends Enumeration {
  protected case class Val(name: String) extends super.Val(name)
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  val OldTownHall: Val = Val("Old Town Hall")
  val Clarks: Val = Val("6th/Belleview (Clarks)")
  val FourWayUphill: Val = Val("4-way (To Mountain)")
  val TeocalliUphill: Val = Val("Teocalli (To Mountain)")
  val MountaineerSquare: Val = Val("Mountaineer Square")
  val TeocalliDownhill: Val = Val("Teocalli (To Downtown)")
  val FourwayDownhill: Val = Val("4-way (To Downtown)")
}
