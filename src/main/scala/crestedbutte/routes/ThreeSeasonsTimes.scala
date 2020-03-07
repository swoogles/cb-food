package crestedbutte.routes

import crestedbutte.time.BusDuration
import crestedbutte.{
  BusSchedule,
  BusScheduleAtStop,
  Location,
  NamedRoute,
  RouteName
}
import crestedbutte.time.BusDuration.toBusDuration

/*

  // TODO How should I indicate when it arrives back at Mountaineer Square?
  //    Currently, it's hard to determine what buses you could catch from the Square.
 */
object ThreeSeasonsTimes
    extends NamedRoute(
      RouteName.ThreeSeasonsLoop,
      RouteWithTimes(
        BusScheduleAtStop(
          Location.MountaineerSquare,
          BusSchedule("08:00", "22:45", 15.minutes)
        ),
        Seq(
          (Location.ThreeSeasons, 1.minutes),
          (Location.MountainSunrise, 1.minutes),
          (Location.UpperChateaux, 0.minutes),
          (Location.LowerChateaux, 1.minutes)
        )
      )
    )
