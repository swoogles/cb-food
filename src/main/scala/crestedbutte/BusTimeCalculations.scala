package crestedbutte

import crestedbutte.time.BusTime

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

}
