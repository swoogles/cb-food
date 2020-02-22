package crestedbutte

object Location extends Enumeration {

  protected case class Val(name: String,
                           gpsCoordinates: GpsCoordinates)
      extends super.Val(name)
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  // TODO Check ordering of all coordinates
  val OldTownHall: Val =
    Val("Old Town Hall", GpsCoordinates(38.869538, -106.987547))

  val Clarks: Val = Val("6th/Belleview (Clarks)",
                        GpsCoordinates(38.866970, -106.981499)) //

  val FourWayUphill: Val = Val(
    "4-way (To Mountain)",
    GpsCoordinates(38.870355, -106.980905)
  ) // gps
  val TeocalliUphill: Val = Val(
    "Teocalli (To Mountain)",
    GpsCoordinates(38.872718, -106.980830)
  ) //
  val MountaineerSquare: Val = Val(
    "Mountaineer Square",
    GpsCoordinates(38.900902, -106.966650)
  ) //  // This is rough. Maps seems to be off...
  val TeocalliDownhill: Val = Val(
    "Teocalli (To Downtown)",
    GpsCoordinates(38.872726, -106.981037)
  ) //
  val FourwayDownhill: Val = Val(
    "4-way (To Downtown)",
    GpsCoordinates(38.869944, -106.981503)
  ) //

  // Condo loop entries
  val ThreeSeasons: Val = Val(
    "Three Seasons",
    GpsCoordinates(0, 0)
  )

  val MountainSunrise: Val = Val(
    "Mountain Sunrise",
    GpsCoordinates(0, 0)
  )

  val UpperChateaux: Val = Val(
    "Upper Chateaux",
    GpsCoordinates(0, 0)
  )

  val LowerChateaux: Val = Val(
    "Lower Chateaux / Marcellina",
    GpsCoordinates(0, 0)
  )
  /*
  Mountain Sunrise	  :02, :17, :32, :47	8:02 AM	  10:47 PM
    Upper Chateaux
    Right after Mtn Sunrise
    Lower Chateaux,
  Marcellina	      :03, :18, :33, :48	8:03 AM	  10:48 PM

 */
}
