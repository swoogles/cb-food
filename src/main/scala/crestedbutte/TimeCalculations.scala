package crestedbutte

import crestedbutte.time.BusTime
import zio.ZIO
import zio.clock.Clock

object TimeCalculations {

  def nextBusArrivalTime(timesAtStop: Seq[BusTime],
                         now: BusTime): Option[BusTime] =
    timesAtStop
      .find(stopTime => BusTime.catchableBus(now, stopTime))
      .filter(_ => now.isLikelyEarlyMorningRatherThanLateNight)

  def getUpcomingArrivalInfo(
    stops: BusScheduleAtStop,
    now: BusTime
  ): UpcomingArrivalInfo =
    nextBusArrivalTime(stops.times, now)
      .map(
        nextArrivalTime =>
          UpcomingArrivalInfo(
            stops.location,
            StopTimeInfo(
              nextArrivalTime,
              nextArrivalTime
                .between(now)
            )
          )
      )
      .getOrElse(
        UpcomingArrivalInfo(
          stops.location,
          LateNightRecommendation("Late Shuttle")
        )
      )

  def calculateUpcomingArrivalAtAllStops(
    now: BusTime,
    busRoute: NamedRoute
  ): Seq[UpcomingArrivalInfo] =
    busRoute.schedules.map(
      scheduleAtStop => getUpcomingArrivalInfo(scheduleAtStop, now)
    )

  def calculateUpcomingArrivalWithFullScheduleAtAllStops(
    now: BusTime,
    busRoute: NamedRoute
  ): Seq[UpcomingArrivalInfoWithFullSchedule] =
    busRoute.schedules.map(
      scheduleAtStop =>
        UpcomingArrivalInfoWithFullSchedule(
          getUpcomingArrivalInfo(scheduleAtStop, now),
          scheduleAtStop.scheduleAfter(now)
        )
    )

  def getUpComingArrivals(
    busRoute: NamedRoute
  ): ZIO[Clock, Nothing, Seq[UpcomingArrivalInfo]] =
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      localTime = new BusTime(now.toLocalTime)
    } yield {
      TimeCalculations.calculateUpcomingArrivalAtAllStops(
        localTime,
        busRoute
      )
    }

  def getUpComingArrivalsWithFullSchedule(
    busRoute: NamedRoute
  ): ZIO[Clock, Nothing, UpcomingArrivalComponentData] =
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      localTime = new BusTime(now.toLocalTime)
    } yield {
      UpcomingArrivalComponentData(
        TimeCalculations
          .calculateUpcomingArrivalWithFullScheduleAtAllStops(
            localTime,
            busRoute
          ),
        busRoute.routeName
      )
    }
}
