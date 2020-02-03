package crestedbutte

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalTime}

import org.scalajs.dom.html.Div
import scalatags.JsDom

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

  private val startTime = LocalTime.parse("07:10:00")
  private val endTime = LocalTime.parse("23:40:00")

  private val totalBusRunTime =
    java.time.Duration.between(startTime, endTime)

  private val numberOfBusesPerDay = totalBusRunTime.getSeconds / java.time.Duration
      .ofMinutes(15)
      .getSeconds

  val oldTownHallBusStarts: Stops =
    Stops(StopLocation.OldTownHall,
          List
            .range(0, numberOfBusesPerDay)
            .map(
              index =>
                startTime.plus(
                  java.time.Duration.ofMinutes(15).multipliedBy(index)
                )
            ))

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

  def nextBusArrivalTime(timesAtStop: Seq[LocalTime],
                         localTime: LocalTime): Option[LocalTime] =
    localTime match {
      case localTime: LocalTime
          if localTime.isAfter(LocalTime.parse("04:00:00")) =>
        timesAtStop
          .find(
            stopTime =>
              stopTime
                .isAfter(localTime.truncatedTo(ChronoUnit.MINUTES))
          )
      case _ => Option.empty
    }

  def nextBusTime(
    stops: Stops,
    localTime: LocalTime
  ): UpcomingArrivalInfo = // TODO use ZIO.option
    BusTimes
      .nextBusArrivalTime(stops.times, localTime)
      .map(
        nextArrivalTime =>
          UpcomingArrivalInfo(
            stops.location,
            Left(
              StopTimeInfo(
                nextArrivalTime,
                Duration.between(nextArrivalTime, localTime).abs()
              )
            )
          )
      )
      .map { case x => { println(localTime); x; } }
      .getOrElse(
        UpcomingArrivalInfo(
          stops.location,
          Right(SafeRideRecommendation("safe-ride"))
        )
      )

  def calculateUpcomingArrivalAtAllStops(
    localTime: LocalTime
  ): List[UpcomingArrivalInfo] =
    townShuttleStops.map(nextBusTime(_, localTime))

  val townShuttleStops = List(
    oldTownHallBusStarts,
    clarksBusStarts,
    fourWayUphillBusStarts,
    teocalliUphillBusStarts,
    mountaineerSquareBusStarts,
    teocalliDownhillBusStarts,
    fourwayDownhill
  )
}
