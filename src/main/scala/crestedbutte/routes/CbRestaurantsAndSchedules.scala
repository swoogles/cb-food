package crestedbutte.routes

import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte.{
  BusSchedule,
  Location,
  NamedRoute,
  PhoneNumber,
  RestaurantWithSchedule,
  RouteName,
}

object CbRestaurantsAndSchedules
    extends NamedRoute(
      RouteName("Crested Butte Restaurants"),
      RestaurantGroup(
        Seq(
          RestaurantWithSchedule(
            Location.SecretStash,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-209-5159", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.Pitas,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-0897", "Order!"),
          ),
//        Seq(
//          (Location.Clarks, 5.minutes),
//          (Location.Pitas, 1.minutes),
//          (Location.TeocalliUphill, 1.minutes),
//          (Location.WoodCreekMountainEdge, 4.minutes),
//          (Location.ThePlaza, 1.minutes),
//          (Location.MountaineerSquare, 13.minutes),
//          (Location.ThreeSeasons, 1.minutes),
//          (Location.MountainSunrise, 0.minutes), // TODO Confirm
//          (Location.Pitchfork, 5.minutes),
        ),
      ),
    )
