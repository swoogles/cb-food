package crestedbutte.routes

import crestedbutte.time.BusDuration
import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte.{BusSchedule, BusScheduleAtStop, Location}

object CrystalCastleShuttle {

  /*
  Every 30 minutes from 8:10 AM to 10:40 PM
   *From 11:00 p.m. until midnight, a condo bus will run from Mountaineer Square
   to a designated stop on a condo route, on demand only.

    Stop	                    Times	    First Bus	Last Bus
    Mountaineer Square	      :10, :40	8:10 AM	  10:40 PM
    Pitchfork	                :12, :42	8:12 AM	  10:42 PM
    Crystal Road	            :13, :43	8:13 AM	  10:43 PM
    Castle Road	              :15, :45	8:15 AM	  10:45 PM
    Wood Creek/Mountain Edge	:16, :46	8:16 AM	  10:46 PM
    Hunter Hill/Timberline	  :18, :48	8:18 AM	  10:48 PM

   */
  val mountaineerSquare: BusScheduleAtStop =
    BusScheduleAtStop(
      Location.MountaineerSquare,
      BusSchedule("08:10", "22:40", 30.minutes)
    )

  val locationsWithDelays: Seq[(Location.Value, BusDuration)] =
    Seq(
      (Location.Pitchfork, 2.minutes),
      (Location.CrystalRoad, 1.minutes),
      (Location.CastleRoad, 2.minutes),
      (Location.WoodCreekMountainEdge, 1.minutes),
      (Location.HunterHillTimberline, 2.minutes)
    )

  val allStops: Seq[BusScheduleAtStop] =
    locationsWithDelays
      .foldLeft(Seq(mountaineerSquare)) {
        case (stopsSoFar, currentStop) =>
          stopsSoFar :+ stopsSoFar.last
            .delayedBy(currentStop._2)
            .at(currentStop._1)
      }

  // TODO How should I indicate when it arrives back at Mountaineer Square?
  //    Currently, it's hard to determine what buses you could catch from the Square.

}
