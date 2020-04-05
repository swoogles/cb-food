package crestedbutte.routes

import crestedbutte.{Location, RestaurantWithSchedule}
import crestedbutte.time.BusDuration

class RouteWithTimes(
  val allStops: Seq[RestaurantWithSchedule],
) {

  def combinedWith(routeWithTimes: RouteWithTimes): RouteWithTimes =
    new RouteWithTimes(
      (allStops ++: routeWithTimes.allStops)
        .foldLeft(Seq[RestaurantWithSchedule]()) {
          case (stopsAcc: Seq[RestaurantWithSchedule], nextStop) =>
            val combinedStop =
              stopsAcc.find(_.location == nextStop.location).map {
                existingStop =>
                  RestaurantWithSchedule.combine(existingStop,
                                                 nextStop)
              }
            if (combinedStop.isDefined) {
              val (beforeStop, replacedStop :: afterStop) =
                stopsAcc.splitAt(
                  stopsAcc.indexWhere(
                    _.location == nextStop.location,
                  ),
                )
              beforeStop :+ combinedStop.get :++ afterStop
            } else {
              stopsAcc :+ nextStop
            }

        },
    )
}

object RouteWithTimes {

  def apply(
    originStops: RestaurantWithSchedule,
    locationsWithDelays: Seq[(Location.Value, BusDuration)],
  ) =
    new RouteWithTimes(
      locationsWithDelays
        .foldLeft(Seq(originStops)) {
          case (stopsSoFar, currentStop) =>
            stopsSoFar :+ stopsSoFar.last
              .delayedBy(currentStop._2)
              .at(currentStop._1)
        },
    )

}
