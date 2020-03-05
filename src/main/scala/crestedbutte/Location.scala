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

  val TallTexan: Val =
    Val("TallTexan",
        GpsCoordinates(0, 0),
        "tall_texan",
        "(Flag Stop)")

  val OhioCreek: Val =
    Val("OhioCreek",
        GpsCoordinates(0, 0),
        "ohio_creek",
        "(Flag Stop)")

  val Almont: Val =
    Val("Almont", GpsCoordinates(0, 0), "almont", "(Flag Stop)")

  val CBSouth: Val =
    Val("CB South",
        GpsCoordinates(0, 0),
        "CB_South",
        "(Red Mtn Park)")

  val Riverland: Val =
    Val("Riverland", GpsCoordinates(0, 0), "Riverland", "(Flag Stop)")

  val BrushCreek: Val =
    Val("Brush Creek",
        GpsCoordinates(0, 0),
        "brush_creek",
        "(Flag Stop)")

  val Riverbend: Val =
    Val("Riverbend", GpsCoordinates(0, 0), "Riverbend", "(Flag Stop)")

  // BEGIN Condo loop stops
  val Whetstone: Val =
    Val("Whetstone", GpsCoordinates(0, 0), "Whetstone", "")

  val ColumbineCondo: Val =
    Val("ColumbineCondo", GpsCoordinates(0, 0), "ColumbineCondo", "")

  val CinnamonMtn: Val =
    Val("Cinnamon Mtn / Gothic",
        GpsCoordinates(0, 0),
        "Cinnamon_Mtn_Gothic",
        "")

  val MtCbTownHall: Val =
    Val("Mt CB Town Hall", GpsCoordinates(0, 0), "MtCbTownHall", "")

  val UpperParadiseRoad: Val =
    Val("UpperParadiseRoad",
        GpsCoordinates(0, 0),
        "UpperParadiseRoad",
        "")

  val LowerParadiseRoad: Val =
    Val("LowerParadiseRoad",
        GpsCoordinates(0, 0),
        "LowerParadiseRoad",
        "")

  val EaglesNestCondos: Val =
    Val("EaglesNestCondos",
        GpsCoordinates(0, 0),
        "EaglesNestCondos",
        "")
  // END Condo loop stops

}
