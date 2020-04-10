package crestedbutte.routes

import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte.{
  BusSchedule,
  Location,
  NamedRoute,
  PhoneNumber,
  RestaurantWithSchedule,
  RouteName,
  Website,
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
            Website("http://brickovencb.com/"),
          ),
          RestaurantWithSchedule(
            Location.Bonez,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-5118", "Order!"),
            Website("https://www.bonez.co/"),
          ),
          RestaurantWithSchedule(
            Location.CoalCreekGrill,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-6645", "Order!"),
            Website("http://www.coalcreekgrill.com/"),
          ),
          RestaurantWithSchedule(
            Location.Dogwood,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-6338", "Order!"),
            Website("https://thedogwoodcb.wordpress.com/"),
          ),
          RestaurantWithSchedule(
            Location.McGills,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-5240", "Order!"),
            Website("https://www.mcgillscrestedbutte.com/"),
          ),
          RestaurantWithSchedule(
            Location.Pitas,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-0897", "Order!"),
            Website("http://pitasinparadise.com/"),
          ),
          RestaurantWithSchedule(
            Location.SecretStash,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-209-5159", "Order!"),
            Website("http://www.secretstash.com/"),
          ),
          RestaurantWithSchedule(
            Location.Slogar,
            BusSchedule("03:00", "03:01", 1.minutes),
            PhoneNumber("970-349-5765", "Order!"),
            Website("https://www.slogarcb.com/"),
          ),
        ),
      ),
    )
