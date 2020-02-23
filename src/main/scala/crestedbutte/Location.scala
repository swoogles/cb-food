package crestedbutte

object Location extends Enumeration {

  protected case class Val(name: String,
                           gpsCoordinates: GpsCoordinates,
                           elementName: String)
      extends super.Val(name)
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  // TODO Check ordering of all coordinates
  val OldTownHall: Val =
    Val("Old Town Hall",
        GpsCoordinates(38.869538, -106.987547),
        "old_town_hall")

  val Clarks: Val = Val("6th/Belleview (Clarks)",
                        GpsCoordinates(38.866970, -106.981499),
                        "clarks") //

  val FourWayUphill: Val = Val("4-way (To Mountain)",
                               GpsCoordinates(38.870355, -106.980905),
                               "fourway_uphill") // gps
  val TeocalliUphill: Val = Val(
    "Teocalli (To Mountain)",
    GpsCoordinates(38.872718, -106.980830),
    "teocalli_uphill"
  ) //
  val MountaineerSquare: Val = Val(
    "Mountaineer Square",
    GpsCoordinates(38.900902, -106.966650),
    "mountaineer_square"
  ) //  // This is rough. Maps seems to be off...
  val TeocalliDownhill: Val = Val(
    "Teocalli (To Downtown)",
    GpsCoordinates(38.872726, -106.981037),
    "teocalli_downhill"
  ) //
  val FourwayDownhill: Val = Val(
    "4-way (To Downtown)",
    GpsCoordinates(38.869944, -106.981503),
    "fourway_downhill"
  ) //

  // Condo loop entries
  val ThreeSeasons: Val =
    Val("Three Seasons", GpsCoordinates(0, 0), "three_seasons")

  val MountainSunrise: Val =
    Val("Mountain Sunrise", GpsCoordinates(0, 0), "mountain_sunrise")

  val UpperChateaux: Val =
    Val("Upper Chateaux", GpsCoordinates(0, 0), "upper_chateaux")

  val LowerChateaux: Val = Val("Lower Chateaux / Marcellina",
                               GpsCoordinates(0, 0),
                               "lower_chateaux")
  /*
  Mountain Sunrise	  :02, :17, :32, :47	8:02 AM	  10:47 PM
    Upper Chateaux
    Right after Mtn Sunrise
    Lower Chateaux,
  Marcellina	      :03, :18, :33, :48	8:03 AM	  10:48 PM

 */
}
