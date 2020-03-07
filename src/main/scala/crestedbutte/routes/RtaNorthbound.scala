package crestedbutte.routes

import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte.{
  BusSchedule,
  BusScheduleAtStop,
  Location,
  NamedRoute,
  RouteName
}

object RtaNorthbound {

  val expressRouteWithTimes =
    RouteWithTimes(
      BusScheduleAtStop(
        Location.GunnisonCommunitySchools,
        BusSchedule(
          "06:30",
          "06:45",
          "07:00",
          "07:30",
          "08:00",
          "08:30"
        )
      ),
      Seq(
        (Location.EleventhAndVirginia, 2.minutes),
        (Location.Safeway, 4.minutes),
        (Location.TellerAndHighwayFifty, 2.minutes),
        (Location.Western, 2.minutes),
        (Location.DenverAndHighwayOneThirtyFive, 3.minutes),
        (Location.SpencerAndHighwayOneThirtyFive, 2.minutes),
        (Location.TallTexan, 2.minutes),
        (Location.OhioCreek, 1.minutes),
        (Location.Almont, 7.minutes),
        (Location.Riverland, 16.minutes),
        (Location.BrushCreek, 1.minutes),
        (Location.Riverbend, 1.minutes),
        (Location.FourWayUphill, 3.minutes),
        (Location.MountaineerSquare, 10.minutes)
      )
    )

  val routeWithTimes =
    RouteWithTimes(
      BusScheduleAtStop(
        Location.GunnisonCommunitySchools,
        BusSchedule(
          "05:30",
          "06:00",
          "09:00",
          "09:30",
          "10:00",
          "10:30",
          "11:05",
          "11:35",
          "12:05",
          "13:00",
          "13:30",
          "14:15",
          "15:05",
          "15:30",
          "16:00",
          "16:30",
          "17:00",
          "18:00",
          "19:05",
          "20:05",
          "21:10",
          "22:10"
        )
      ),
      Seq(
        (Location.EleventhAndVirginia, 2.minutes),
        (Location.Safeway, 4.minutes),
        (Location.TellerAndHighwayFifty, 2.minutes),
        (Location.Western, 2.minutes),
        (Location.DenverAndHighwayOneThirtyFive, 3.minutes),
        (Location.SpencerAndHighwayOneThirtyFive, 2.minutes),
        (Location.TallTexan, 2.minutes),
        (Location.OhioCreek, 1.minutes),
        (Location.Almont, 7.minutes),
        (Location.CBSouth, 17.minutes),
        (Location.Riverland, 5.minutes),
        (Location.BrushCreek, 1.minutes),
        (Location.Riverbend, 1.minutes),
        (Location.FourWayUphill, 3.minutes),
        (Location.MountaineerSquare, 10.minutes)
      )
    )

  val fullSchedule = NamedRoute(
    RouteName.RtaNorthbound,
    expressRouteWithTimes.combinedWith(expressRouteWithTimes)
  )

}
