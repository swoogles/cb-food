package crestedbutte

import java.time.LocalTime

import zio.ZIO
import zio.clock.Clock

object BusTimes {
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

  private val startTime = BusTime.parse("07:10:00")
  private val endTime = BusTime.parse("23:40:00")

  private val totalBusRunTime =
    startTime.between(endTime)

  private val numberOfBusesPerDay =
    totalBusRunTime.dividedByMinutes(15)

  val oldTownHallBusStarts: Stops =
    Stops(
      StopLocation.OldTownHall,
      List
        .range(0, numberOfBusesPerDay)
        .map(
          index => startTime.plusMinutes(15 * index.toInt)
        )
    )

  val clarksBusStarts: Stops =
    Stops(StopLocation.Clarks,
          oldTownHallBusStarts.times
            .map(_.plusMinutes(4)))

  val fourWayUphillBusStarts: Stops =
    Stops(StopLocation.FourWayUphill,
          clarksBusStarts.times
            .map(_.plusMinutes(1)))

  val teocalliUphillBusStarts: Stops =
    Stops(StopLocation.TeocalliUphill,
          fourWayUphillBusStarts.times
            .map(_.plusMinutes(1)))

  val mountaineerSquareBusStarts: Stops =
    Stops(StopLocation.MountaineerSquare,
          teocalliUphillBusStarts.times
            .map(_.plusMinutes(14)))

  val teocalliDownhillBusStarts: Stops =
    Stops(StopLocation.TeocalliDownhill,
          mountaineerSquareBusStarts.times
            .map(_.plusMinutes(6)))

  val fourwayDownhill: Stops =
    Stops(StopLocation.FourwayDownhill,
          teocalliDownhillBusStarts.times
            .map(_.plusMinutes(1)))

  def nextBusArrivalTime(timesAtStop: Seq[BusTime],
                         now: BusTime): Option[BusTime] =
    now match {
      case localTime: BusTime
          if localTime.tooLateToBeConsideredLateNight =>
        timesAtStop
          .find(
            stopTime => BusTime.catchableBus(localTime, stopTime)
          )
      case _ => Option.empty
    }

  def nextBusTime(
    stops: Stops,
    now: BusTime
  ): UpcomingArrivalInfo = // TODO use ZIO.option
    BusTimes
      .nextBusArrivalTime(stops.times, now)
      .map(
        nextArrivalTime =>
          UpcomingArrivalInfo(
            stops.location,
            Left(
              StopTimeInfo(
                nextArrivalTime,
                nextArrivalTime
                  .between(now)
              )
            )
          )
      )
      .map(x => {
        println(now); x;
      })
      .getOrElse(
        UpcomingArrivalInfo(
          stops.location,
          Right(SafeRideRecommendation("safe-ride"))
        )
      )

  def calculateUpcomingArrivalAtAllStops(
    now: BusTime
  ): List[UpcomingArrivalInfo] =
    townShuttleStops.map(nextBusTime(_, now))

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
    } yield { BusTimes.calculateUpcomingArrivalAtAllStops(localTime) }
}
