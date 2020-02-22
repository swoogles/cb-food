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
    busRoute: Route
  ): Seq[UpcomingArrivalInfo] =
    busRoute.schedules.map(
      scheduleAtStop => getUpcomingArrivalInfo(scheduleAtStop, now)
    )

  def calculateUpcomingArrivalWithFullScheduleAtAllStops(
    now: BusTime,
    busRoute: Route
  ): Seq[UpcomingArrivalInfoWithFullSchedule] =
    busRoute.schedules.map(
      scheduleAtStop =>
        UpcomingArrivalInfoWithFullSchedule(
          getUpcomingArrivalInfo(scheduleAtStop, now),
          scheduleAtStop
        )
    )

  def getUpComingArrivals(
    busRoute: Route
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
    busRoute: Route
  ): ZIO[Clock, Nothing, Seq[UpcomingArrivalInfoWithFullSchedule]] =
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      localTime = new BusTime(now.toLocalTime)
    } yield {
      TimeCalculations
        .calculateUpcomingArrivalWithFullScheduleAtAllStops(
          localTime,
          busRoute
        )
    }
}
