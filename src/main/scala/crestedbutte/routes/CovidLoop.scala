package crestedbutte.routes

import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte.{
  BusSchedule,
  BusScheduleAtStop,
  Location,
  NamedRoute,
  RouteName,
}

object CovidLoop
    extends NamedRoute(
      RouteName("Covid Loop"),
      RouteWithTimes(
        BusScheduleAtStop(
          Location.OldTownHall,
          BusSchedule("07:35", "23:35", 20.minutes),
        ),
        Seq(
          (Location.Clarks, 5.minutes),
          (Location.FourWayUphill, 1.minutes),
          (Location.TeocalliUphill, 1.minutes),
          (Location.WoodCreekMountainEdge, 4.minutes),
          (Location.ThePlaza, 1.minutes),
          (Location.MountaineerSquare, 13.minutes),
          (Location.ThreeSeasons, 1.minutes),
          (Location.MountainSunrise, 0.minutes), // TODO Confirm
          (Location.Pitchfork, 5.minutes),
        ),
      ),
    )
/*
  Stop	                  Times	          First Bus	Last Bus
  Old Town Hall	          :15, :35, :55	  7:35 AM	  11:35 PM
  6th & Belleview	        :00, :20, :40	  7:40 AM	  11:40 PM
  4 Way Stop	            :01, :21, :41	  7:41 AM	  11:41 PM
  Teocalli	              :02, :22, :42	  7:42 AM	  11:42 PM
  Wood Creek/Mtn Edge	    :06, :26, :46	  7:46 AM	  11:46 PM
  The Plaza	              :07, :27, :47	  7:47 AM	  11:47 PM
  Mountaineer Square	    :00, :20, :40	  8:00 AM	  12:00 AM
  Three Seasons, Chateaux :01, :21, :41	  8:01 AM	  12:01 AM
  Pitchfork 	            :06, :26, :46	  8:06 AM	  12:06 AM
 */
