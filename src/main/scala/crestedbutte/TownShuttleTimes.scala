package crestedbutte

import crestedbutte.time.BusDuration.toBusDuration // Enables Int.minutes syntax

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
      BusSchedule("07:10:00", "23:40:00", 15.minutes)
    )

  val clarksBusStarts: BusScheduleAtStop =
    oldTownHallBusStarts
      .delayedBy(4.minutes)
      .at(Location.Clarks)

  val fourWayUphillBusStarts: BusScheduleAtStop =
    clarksBusStarts
      .delayedBy(1.minutes)
      .at(Location.FourWayUphill)

  val teocalliUphillBusStarts: BusScheduleAtStop =
    fourWayUphillBusStarts
      .delayedBy(1.minutes)
      .at(Location.TeocalliUphill)

  val mountaineerSquareBusStarts: BusScheduleAtStop =
    teocalliUphillBusStarts
      .delayedBy(14.minutes)
      .at(Location.MountaineerSquare)

  val teocalliDownhillBusStarts: BusScheduleAtStop =
    mountaineerSquareBusStarts
      .delayedBy(6.minutes)
      .at(Location.TeocalliDownhill)

  val fourwayDownhill: BusScheduleAtStop =
    teocalliDownhillBusStarts
      .delayedBy(1.minutes)
      .at(Location.FourwayDownhill)

  val townShuttleStops: Seq[BusScheduleAtStop] = Seq(
    oldTownHallBusStarts,
    clarksBusStarts,
    fourWayUphillBusStarts,
    teocalliUphillBusStarts,
    mountaineerSquareBusStarts,
    teocalliDownhillBusStarts,
    fourwayDownhill
  )

}
