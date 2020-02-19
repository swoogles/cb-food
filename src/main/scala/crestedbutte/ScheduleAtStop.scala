package crestedbutte

import crestedbutte.time.BusTime

case class ScheduleAtStop(stopTimes: List[BusTime]) {

  def nextBusArrivalTime(now: BusTime): Option[BusTime] =
    stopTimes
      .find(stopTime => BusTime.catchableBus(now, stopTime))
      .filter(_ => now.tooLateToBeConsideredLateNight)
}

object ScheduleAtStop {

  def apply(stopTimeStrings: String*) =
    new ScheduleAtStop(
      List(stopTimeStrings: _*)
        .map(BusTime(_))
    )
}
