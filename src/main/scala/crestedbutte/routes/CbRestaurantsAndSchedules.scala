package crestedbutte.routes

import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte.{
  BusSchedule,
  Location,
  NamedRoute,
  PhoneNumber,
  RestaurantGroupName,
  RestaurantWithSchedule,
  Website,
}

object CbRestaurantsAndSchedules
    extends RestaurantGroup(
      RestaurantGroupName("Crested Butte Restaurants"),
      Seq(
        RestaurantWithSchedule(
          Location.BrickOven,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-349-5044", "Order!"),
          Website.global("http://brickovencb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/BrickOvenCB/",
          ),
        ),
        RestaurantWithSchedule(
          Location.Bonez,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-349-5118", "Order!"),
          Website.global("https://www.bonez.co/"),
          Website.facebookPage(
            "https://www.facebook.com/BONEZ-662144153840370/",
          ),
        ),
        RestaurantWithSchedule(
          Location.CoalCreekGrill,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-349-6645", "Order!"),
          Website.global("http://www.coalcreekgrill.com/"),
          Website.facebookPage(
            "https://www.facebook.com/CoalCreekGrillCB/",
          ),
        ),
        RestaurantWithSchedule(
          Location.Dogwood,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-349-6338", "Order!"),
          Website.global("https://thedogwoodcb.wordpress.com/"),
          Website.facebookPage(
            "https://www.facebook.com/thedogwoodcb/",
          ),
        ),
        RestaurantWithSchedule(
          Location.McGills,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-349-5240", "Order!"),
          Website.global("https://www.mcgillscrestedbutte.com/"),
          Website.facebookPage(
            "https://www.facebook.com/pages/McGills-At-Crested-Butte/119847854694618",
          ),
        ),
        RestaurantWithSchedule(
          Location.Pitas,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-349-0897", "Order!"),
          Website.global("http://pitasinparadise.com/"),
          Website.facebookPage(
            "https://www.facebook.com/PitasInParadise/",
          ),
        ),
        RestaurantWithSchedule(
          Location.SecretStash,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-209-5159", "Order!"),
          Website.global("http://www.secretstash.com/"),
          Website.facebookPage(
            "https://www.facebook.com/TheSecretStashPizza",
          ),
        ),
        RestaurantWithSchedule(
          Location.Slogar,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-349-5765", "Order!"),
          Website.global("https://www.slogarcb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/slogarqueen/",
          ),
        ),
        RestaurantWithSchedule(
          Location.Tullys,
          BusSchedule("03:00", "03:01", 1.minutes),
          PhoneNumber("970-349-2444", "Order!"),
          Website.global("https://www.tullyscbsouth.com/"),
          Website.facebookPage(
            "https://www.facebook.com/tullys456/",
          ),
        ),
      ),
    ) {}
