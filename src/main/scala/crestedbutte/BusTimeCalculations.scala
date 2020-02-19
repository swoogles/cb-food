package crestedbutte

import crestedbutte.time.BusTime
import zio.ZIO
import zio.clock.Clock

object BusTimeCalculations {

  def nextBusArrivalTime(timesAtStop: Seq[BusTime],
                         now: BusTime): Option[BusTime] =
    timesAtStop
      .find(stopTime => BusTime.catchableBus(now, stopTime))
      .filter(_ => now.tooLateToBeConsideredLateNight)

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
          SafeRideRecommendation("safe-ride")
        )
      )

  def calculateUpcomingArrivalAtAllStops(
                                          now: BusTime,
                                          busRoute: BusRoute
                                        ): Seq[UpcomingArrivalInfo] =
    busRoute.schedules.map(
      getUpcomingArrivalInfo(_, now)
    )

  def getUpComingArrivals(busRoute: BusRoute)
  : ZIO[Clock, Nothing, Seq[UpcomingArrivalInfo]] =
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      localTime = new BusTime(now.toLocalTime)
    } yield {
      BusTimeCalculations.calculateUpcomingArrivalAtAllStops(localTime, busRoute)
    }
}
