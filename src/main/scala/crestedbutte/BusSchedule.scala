package crestedbutte

import crestedbutte.time.{BusDuration, BusTime}

case class BusSchedule(stopTimes: List[BusTime]) {

  def nextBusArrivalTime(now: BusTime): Option[BusTime] =
    stopTimes
      .find(stopTime => BusTime.catchableBus(now, stopTime))
      .filter(_ => now.tooLateToBeConsideredLateNight)
}

object BusSchedule {

  def apply(firstBus: String, lastBus: String, timeBetweenBuses: BusDuration) =
    new BusSchedule(
    List
      .range(0, BusTime(firstBus).between(BusTime(lastBus)).dividedBy(timeBetweenBuses))
      .map(
        index => BusTime(firstBus).plus(timeBetweenBuses.times(index.toInt))
      )
    )



  def apply(stopTimeStrings: String*) =
    new BusSchedule(
      List(stopTimeStrings: _*)
        .map(BusTime(_))
    )
}
