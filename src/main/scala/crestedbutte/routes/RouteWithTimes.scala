package crestedbutte.routes

import crestedbutte.{BusScheduleAtStop, Location}
import crestedbutte.time.BusDuration

class RouteWithTimes(
  val allStops: Seq[BusScheduleAtStop]
) {

  def combinedWith(routeWithTimes: RouteWithTimes): RouteWithTimes =
    new RouteWithTimes(
      (allStops ++: routeWithTimes.allStops)
        .foldLeft(Seq[BusScheduleAtStop]()) {
          case (stopsAcc: Seq[BusScheduleAtStop], nextStop) =>
            val combinedStop =
              stopsAcc.find(_.location == nextStop.location).map {
                existingStop =>
                  BusScheduleAtStop.combine(existingStop, nextStop)
              }
            if (combinedStop.isDefined) {
              val (beforeStop, replacedStop :: afterStop) =
                stopsAcc.splitAt(
                  stopsAcc.indexWhere(_.location == nextStop.location)
                )
              beforeStop :+ combinedStop.get :++ afterStop
            } else {
              stopsAcc :+ nextStop
            }

        }
    )
}

object RouteWithTimes {

  def apply(
    originStops: BusScheduleAtStop,
    locationsWithDelays: Seq[(Location.Value, BusDuration)]
  ) =
    new RouteWithTimes(
      locationsWithDelays
        .foldLeft(Seq(originStops)) {
          case (stopsSoFar, currentStop) =>
            stopsSoFar :+ stopsSoFar.last
              .delayedBy(currentStop._2)
              .at(currentStop._1)
        }
    )

}
