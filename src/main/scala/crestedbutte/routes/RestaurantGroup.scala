package crestedbutte.routes

import crestedbutte.RestaurantWithSchedule

case class RestaurantGroup(
  allStops: Seq[RestaurantWithSchedule],
)
