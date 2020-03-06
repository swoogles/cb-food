package crestedbutte.routes

import crestedbutte.{BusScheduleAtStop, Location}
import crestedbutte.routes.SnodgrassShuttle.{
  locationsWithDelays,
  mountaineerSquare
}
import crestedbutte.time.BusDuration

class RouteWithTimes(
  originStops: BusScheduleAtStop,
  locationsWithDelays: Seq[(Location.Value, BusDuration)]
) {

  val allStops: Seq[BusScheduleAtStop] =
    locationsWithDelays
      .foldLeft(Seq(originStops)) {
        case (stopsSoFar, currentStop) =>
          stopsSoFar :+ stopsSoFar.last
            .delayedBy(currentStop._2)
            .at(currentStop._1)
      }

}
