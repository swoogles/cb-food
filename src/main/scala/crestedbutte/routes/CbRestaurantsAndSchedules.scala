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
            Location.BrickOven,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-5044", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.Bonez,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-5118", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.CoalCreekGrill,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-6645", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.Dogwood,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-6338", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.McGills,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-5240", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.Pitas,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-0897", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.SecretStash,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-209-5159", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.Slogar,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-5765", "Order!"),
          ),
          RestaurantWithSchedule(
            Location.TacosLocale,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-7305", "Order!"),
          ),
        ),
      ),
    )
