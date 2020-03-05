package crestedbutte.routes

import crestedbutte.time.BusDuration
import crestedbutte.{BusSchedule, BusScheduleAtStop, Location}
import crestedbutte.time.BusDuration.toBusDuration

object TownShuttleTimes {
  /*
    Regular Winter Schedule: November 27th through April 12th

    Every 15 Minutes from 7:10 AM to Midnight

                          Stop	Times	            First Bus	  Last Bus
    Old Town Hall 	      :10, :25, :40, :55	    7:10 AM	    11:40 PM
    6th and Belleview	    :59, :14, :29, :44	    7:14 AM	    11:44 PM
    4-Way Stop	          :00, :15, :30, :45 	    7:15 AM	    11:45 PM
    Teocalli	            :01, :16, :31, :46	    7:16 AM	    11:46 PM
    Mountaineer Square	  :00, :15, :30, :45	    7:30 AM 	  12:00 AM
   */

  val oldTownHallBusStarts: BusScheduleAtStop =
    BusScheduleAtStop(
      Location.OldTownHall,
      BusSchedule("07:11", "23:40", 15.minutes)
    )

  val locationsWithDelays: Seq[(Location.Value, BusDuration)] =
    Seq(
      (Location.Clarks, 4.minutes),
      (Location.FourWayUphill, 1.minutes),
      (Location.TeocalliUphill, 1.minutes),
      (Location.MountaineerSquare, 14.minutes),
      (Location.TeocalliDownhill, 6.minutes),
      (Location.FourwayDownhill, 1.minutes)
    )

  val townShuttleStops: Seq[BusScheduleAtStop] =
    locationsWithDelays
      .foldLeft(Seq(oldTownHallBusStarts)) {
        case (stopsSoFar, currentStop) =>
          stopsSoFar :+ stopsSoFar.last
            .delayedBy(currentStop._2)
            .at(currentStop._1)
      }

}
