package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}
import crestedbutte.time.BusDuration.toBusDuration // Enables Int.minutes syntax
import zio.ZIO
import zio.clock.Clock

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

  private val startTime = BusTime("07:10:00")
  private val endTime = BusTime("23:40:00")

  private val totalBusRunTime =
    startTime.between(endTime)

  private val numberOfBusesPerDay =
    totalBusRunTime.dividedBy(15.minutes)

  val oldTownHallBusStarts: Stops =
    Stops(
      StopLocation.OldTownHall,
      List
        .range(0, numberOfBusesPerDay)
        .map(
          index => startTime.plus((15 * index.toInt).minutes)
        )
    )

  val clarksBusStarts: Stops =
    oldTownHallBusStarts
      .delayedBy(4.minutes)
      .at(StopLocation.Clarks)

  val fourWayUphillBusStarts: Stops =
    clarksBusStarts
      .delayedBy(1.minutes)
      .at(StopLocation.FourWayUphill)

  val teocalliUphillBusStarts: Stops =
    fourWayUphillBusStarts
      .delayedBy(1.minutes)
      .at(StopLocation.TeocalliUphill)

  val mountaineerSquareBusStarts: Stops =
    teocalliUphillBusStarts
      .delayedBy(14.minutes)
      .at(StopLocation.MountaineerSquare)

  val teocalliDownhillBusStarts: Stops =
    mountaineerSquareBusStarts
      .delayedBy(6.minutes)
      .at(StopLocation.TeocalliDownhill)

  val fourwayDownhill: Stops =
    teocalliDownhillBusStarts
      .delayedBy(1.minutes)
      .at(StopLocation.FourwayDownhill)

  def calculateUpcomingArrivalAtAllStops(
    now: BusTime
  ): List[UpcomingArrivalInfo] =
    townShuttleStops.map(
      BusTimeCalculations.getUpcomingArrivalInfo(_, now)
    )

  val townShuttleStops = List(
    oldTownHallBusStarts,
    clarksBusStarts,
    fourWayUphillBusStarts,
    teocalliUphillBusStarts,
    mountaineerSquareBusStarts,
    teocalliDownhillBusStarts,
    fourwayDownhill
  )

  val getUpComingArrivals
    : ZIO[Clock, Nothing, List[UpcomingArrivalInfo]] =
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      localTime = new BusTime(now.toLocalTime)
    } yield {
      TownShuttleTimes.calculateUpcomingArrivalAtAllStops(localTime)
    }
}
