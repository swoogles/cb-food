package crestedbutte.routes

import crestedbutte.time.BusDuration
import crestedbutte.{BusSchedule, BusScheduleAtStop, Location}
import crestedbutte.time.BusDuration.toBusDuration

object ThreeSeasonsTimes {

  /*
Stop	              Times	              First Bus	Last Bus
Mountaineer Square	:00, :15, :30, :45	8:00 AM	  10:45 PM
Three Seasons
  /Outrun/Ski Jump	:01, :16, :31, :46	8:01 AM	  10:46 PM
Mountain Sunrise	  :02, :17, :32, :47	8:02 AM	  10:47 PM
Upper Chateaux
  Right after Mtn Sunrise
Lower Chateaux,
  Marcellina	      :03, :18, :33, :48	8:03 AM	  10:48 PM
   */
  val mountaineerSquare: BusScheduleAtStop =
    BusScheduleAtStop(
      Location.MountaineerSquare,
      BusSchedule("08:00", "22:45", 15.minutes)
    )

  val locationsWithDelays: Seq[(Location.Value, BusDuration)] =
    Seq(
      (Location.ThreeSeasons, 1.minutes),
      (Location.MountainSunrise, 1.minutes),
      (Location.UpperChateaux, 0.minutes),
      (Location.LowerChateaux, 1.minutes)
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
