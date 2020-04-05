package crestedbutte.routes

import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte._

object RtaSouthbound {

  val expressRouteWithTimes =
    RouteWithTimes(
      RestaurantWithSchedule(
        Location.MountaineerSquare,
        BusSchedule(
          "15:15",
          "15:45",
          "16:15",
          "16:45",
          "17:15",
          "17:30",
          "17:45",
        ),
      ),
      Seq(
        (Location.FourwayGunnison, 8.minutes),
        (Location.Riverbend, 3.minutes),
        (Location.BrushCreek, 1.minutes),
        (Location.Riverland, 1.minutes),
        (Location.Almont, 14.minutes),
        (Location.OhioCreek, 8.minutes),
        (Location.TallTexan, 1.minutes),
        (Location.RecCenter, 3.minutes),
        (Location.GunnisonCommunitySchools, 4.minutes),
      ),
    )

  val normalRouteWithTimes =
    RouteWithTimes(
      RestaurantWithSchedule(
        Location.MountaineerSquare,
        BusSchedule(
          "06:40",
          "07:10",
          "07:40",
          "08:10",
          "08:40",
          "09:10",
          "09:40",
          "10:15",
          "10:45",
          "11:15",
          "11:45",
          "12:25",
          "13:25",
          "14:15",
          "14:40",
          "18:15",
          "19:15",
          "20:20",
          "21:20",
          "22:25",
          "23:25",
        ),
      ),
      Seq(
        (Location.FourwayGunnison, 8.minutes),
        (Location.Riverbend, 3.minutes),
        (Location.BrushCreek, 1.minutes),
        (Location.Riverland, 1.minutes),
        (Location.CBSouth, 7.minutes),
        (Location.Almont, 14.minutes),
        (Location.OhioCreek, 8.minutes),
        (Location.TallTexan, 1.minutes),
        (Location.RecCenter, 3.minutes),
        (Location.GunnisonCommunitySchools, 4.minutes),
      ),
    )

  val fullSchedule = NamedRoute(
    RouteName("Rta Southbound"),
    normalRouteWithTimes.combinedWith(expressRouteWithTimes),
  )

}
