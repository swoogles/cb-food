package crestedbutte.routes

import crestedbutte.time.{ClosedAllDay, Hours, HoursOfOperation}
import crestedbutte.{
  Location,
  PhoneNumber,
  RestaurantGroupName,
  RestaurantWithSchedule,
  StandardSchedule,
  Website,
}

object GunnisonRestaurants
    extends RestaurantGroup(
      RestaurantGroupName("Gunnison Restaurants"),
      Seq(
        RestaurantWithSchedule(
          Location("5 B's Bar-B-Q"),
          PhoneNumber("970-641-7360", "Order!"),
          Website.global("https://www.5bsbbq.com/"),
          Website.facebookPage(
            "https://www.facebook.com/5BsBBQ/",
          ),
          StandardSchedule.carryOutAndDelivery(
            HoursOfOperation(
              sunday = ClosedAllDay,
              monday = ClosedAllDay,
              tuesday = Hours("11:00", "19:00"),
              wednesday = Hours("11:00", "19:00"),
              thursday = Hours("11:00", "19:00"),
              friday = Hours("11:00", "19:00"),
              saturday = Hours("11:00", "19:00"),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Agave"),
          PhoneNumber("970-417-7489", "Order!"),
          Website.global("https://www.agavefamilymexican.com/"),
          Website.facebookPage(
            "https://www.facebook.com/agavefamilymexican/",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation.everyDay("10:30", "20:00"),
          ),
        ),
      ),
    )
