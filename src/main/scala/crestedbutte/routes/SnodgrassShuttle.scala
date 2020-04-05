package crestedbutte.routes

import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte.{
  BusSchedule,
  Location,
  NamedRoute,
  RestaurantWithSchedule,
  RouteName,
}

/*
Every 60 minutes from 7:55 AM to 9:55 PM
 *From 11:00 p.m. until midnight, a condo bus will run from
Mountaineer Square to a designated stop on a condo route, on demand only.

  Stop	                                  Times	First Bus	Last Bus
  Mountaineer Square	                    :55	  7:55 AM	  9:55 PM
  Cinnamon Mtn/Gothic to Snodgrass TH	    :56	  7:56 AM	  9:56 PM
  Gothic/Winterset to Snodgrass TH	      :58	  7:58 AM	  9:58 PM
  Snodgrass TH	                          :00	  8:00 AM	  10:00 PM
  Gothic/Winterset to Mountaineer Square  :02	  8:02 AM	  10:02 PM
  Mt CB Town Hall to Mountaineer Square	  :03	  8:03 AM	  10:03 PM
  Paradise Road	:05	8:05 AM	10:05 PM
 */
object SnodgrassShuttle
    extends NamedRoute(
      RouteName("Snodgrass Shuttle"),
      RouteWithTimes(
        RestaurantWithSchedule(
          Location.MountaineerSquare,
          BusSchedule("07:55", "21:55", 60.minutes),
        ),
        Seq(
          (Location.CinnamonMtnGothicToSnodgrass, 1.minutes),
          (Location.GothicWintersetTosnodgrass, 2.minutes),
          (Location.SnodgrassTrailhead, 2.minutes),
          (Location.GothicWintersetToMountaineerSquare, 2.minutes),
          (Location.MtCBTownHallToMountaineerSquare, 1.minutes),
          (Location.ParadiseRoad, 1.minutes),
        ),
      ),
    )
