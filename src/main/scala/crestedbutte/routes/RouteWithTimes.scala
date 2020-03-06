package crestedbutte.routes

import crestedbutte.{BusScheduleAtStop, Location}
import crestedbutte.routes.SnodgrassShuttle.{
  locationsWithDelays,
  mountaineerSquare
}
import crestedbutte.time.BusDuration

class RouteWithTimes(
  mountaineerSquare: BusScheduleAtStop,
  locationsWithDelays: Seq[(Location.Value, BusDuration)]
) {

  val allStops: Seq[BusScheduleAtStop] =
    locationsWithDelays
      .foldLeft(Seq(mountaineerSquare)) {
        case (stopsSoFar, currentStop) =>
          stopsSoFar :+ stopsSoFar.last
            .delayedBy(currentStop._2)
            .at(currentStop._1)
      }

}
