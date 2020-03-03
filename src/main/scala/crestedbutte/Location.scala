package crestedbutte

object Location extends Enumeration {

  protected case class Val(name: String,
                           gpsCoordinates: GpsCoordinates,
                           elementName: String,
                           altName: String)
      extends super.Val(name)
  import scala.language.implicitConversions

  implicit def valueToStopLocationVal(x: Value): Val =
    x.asInstanceOf[Val]

  type StopLocation = Value

  // TODO Check ordering of all coordinates
  val OldTownHall: Val =
    Val("Old Town Hall",
        GpsCoordinates(38.869538, -106.987547),
        "old_town_hall",
        "(Malardi Theater)")

  val Clarks: Val = Val("6th/Belleview",
                        GpsCoordinates(38.866970, -106.981499),
                        "clarks",
                        "(Clarks Grocery)") //

  val FourWayUphill: Val = Val("4-way",
                               GpsCoordinates(38.870355, -106.980905),
                               "fourway_uphill",
                               "(To Mountain)") // gps
  val TeocalliUphill: Val = Val(
    "Teocalli",
    GpsCoordinates(38.872718, -106.980830),
    "teocalli_uphill",
    "(To Mountain)"
  ) //
  val MountaineerSquare: Val = Val(
    "Mountaineer Square",
    GpsCoordinates(38.900902, -106.966650),
    "mountaineer_square",
    "(CBMR)"
  ) //  // This is rough. Maps seems to be off...
  val TeocalliDownhill: Val = Val(
    "Teocalli",
    GpsCoordinates(38.872726, -106.981037),
    "teocalli_downhill",
    "(To Downtown)"
  ) //
  val FourwayDownhill: Val = Val(
    "4-way",
    GpsCoordinates(38.869944, -106.981503),
    "fourway_downhill",
    "(To Downtown)"
  ) //

  // Condo loop entries
  val ThreeSeasons: Val =
    Val("Three Seasons", GpsCoordinates(0, 0), "three_seasons", "")

  val MountainSunrise: Val =
    Val("Mountain Sunrise",
        GpsCoordinates(0, 0),
        "mountain_sunrise",
        "")

  val UpperChateaux: Val =
    Val("Upper Chateaux", GpsCoordinates(0, 0), "upper_chateaux", "")

  val LowerChateaux: Val = Val("Lower Chateaux / Marcellina",
                               GpsCoordinates(0, 0),
                               "lower_chateaux",
                               "")

  // RTA stops.
  val GunnisonCommunitySchools: Val = Val(
    "Gunnison Community Schools",
    GpsCoordinates(0, 0),
    "gunnison_community_schools",
    ""
  )

  val EleventhAndVirginia: Val = Val("Eleventh & Virgina",
                                     GpsCoordinates(0, 0),
                                     "eleventh_and_virginia",
                                     "")

  val Safeway: Val =
    Val("Safeway",
        GpsCoordinates(0, 0),
        "Safeway",
        "(Spruce & Highway 50)")

  val TellerAndHighwayFifty: Val =
    Val("Teller & Highway 50",
        GpsCoordinates(0, 0),
        "teller_and_highway_fifty",
        "")

  val Western: Val =
    Val("Western", GpsCoordinates(0, 0), "Western", "Colorado & Ohio")

  val DenverAndHighwayOneThirtyFive: Val =
    Val("Denver & Highway 135",
        GpsCoordinates(0, 0),
        "denver_and_highway_one_thirty_five",
        "(City Market)")

  val SpencerAndHighwayOneThirtyFive: Val =
    Val("Spencer & Highway 135",
        GpsCoordinates(0, 0),
        "spencer_and_highway_one_thirty_five",
        "(Walmart)")

}
