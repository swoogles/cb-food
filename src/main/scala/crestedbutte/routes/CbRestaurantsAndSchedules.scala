package crestedbutte.routes

import java.time.DayOfWeek

import crestedbutte.interpolators.PhoneNumberValueInterpolator
import crestedbutte.time.{
  ClosedAllDay,
  ClosedForTheDay,
  Hours,
  HoursOfOperation,
}
import crestedbutte.{
  AdvanceOrdersOnly,
  Location,
  PhoneNumber,
  RestaurantGroupName,
  RestaurantWithSchedule,
  StandardSchedule,
  Website,
}

object CbRestaurantsAndSchedules
    extends RestaurantGroup(
      RestaurantGroupName("Crested Butte Restaurants"),
      Seq(
        RestaurantWithSchedule(
          Location.BrickOven,
          PhoneNumber("970-349-5044", "Order!"),
          Website.global("http://brickovencb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/BrickOvenCB/",
          ),
          StandardSchedule(
            deliveryHours = Some(
              HoursOfOperation("17:00", "20:00"),
            ),
            carryOutHours = Some(
              HoursOfOperation("11:00", "20:00"),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location.Bonez,
          PhoneNumber("970-349-5118", "Order!"),
          Website.global("https://www.bonez.co/"),
          Website.facebookPage(
            "https://www.facebook.com/BONEZ-662144153840370/",
          ),
          StandardSchedule(
            deliveryHours = Some(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = Hours("16:30", "19:30"),
                tuesday = Hours("16:30", "19:30"),
                wednesday = Hours("16:30", "19:30"),
                thursday = ClosedAllDay,
                friday = ClosedAllDay,
                saturday = ClosedAllDay,
              ),
            ),
            carryOutHours = Some(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = Hours("16:30", "19:30"),
                tuesday = Hours("16:30", "19:30"),
                wednesday = Hours("16:30", "19:30"),
                thursday = ClosedAllDay,
                friday = ClosedAllDay,
                saturday = ClosedAllDay,
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location.ButteBagels,
          PhoneNumber("970-349-5630", "Order!"),
          Website.global("https://butte-bagels.com/"),
          Website.facebookPage(
            "https://www.facebook.com/pages/category/Bagel-Shop/Butte-Bagels-1225240390980501/",
          ),
          StandardSchedule(
            deliveryHours = None,
            carryOutHours = Some(
              HoursOfOperation(
                sunday = Hours("08:00", "14:00"),
                monday = ClosedAllDay,
                tuesday = ClosedAllDay,
                wednesday = Hours("08:00", "14:00"),
                thursday = Hours("08:00", "14:00"),
                friday = Hours("08:00", "14:00"),
                saturday = Hours("08:00", "14:00"),
              ),
            ),
          ),
        ),
//        RestaurantWithSchedule(
//          Location.CoalCreekGrill,
//          BusSchedule("03:00", "03:01", 1.minutes),
//          PhoneNumber("970-349-6645", "Order!"),
//          Website.global("http://www.coalcreekgrill.com/"),
//          Website.facebookPage(
//            "https://www.facebook.com/CoalCreekGrillCB/",
//          ),
//        ),
        RestaurantWithSchedule(
          Location.TheDivvy,
          PhoneNumber("970-787-5447", "Order!"),
          Website.global("https://thedivvycrestedbutte.com/"),
          Website.facebookPage(
            "https://www.facebook.com/TheDivvyCB/",
          ),
          AdvanceOrdersOnly(
            """Pick-up/Carry Out by appointment only.
              |Will deliver if quarantined.
              |Must order 1 day in advance.
              |Taking orders 7 days a week""".stripMargin,
          ),
        ),
        RestaurantWithSchedule(
          Location.GasCafe,
          PhoneNumber("970-349-9656", "Order!"),
          Website.global("https://www.gascafe1stop.com/"),
          Website.facebookPage(
            "https://www.facebook.com/gascafeonestop/",
          ),
          StandardSchedule(
            deliveryHours = None,
            carryOutHours = Some(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = Hours("07:00", "15:00"),
                tuesday = Hours("07:00", "15:00"),
                wednesday = Hours("07:00", "15:00"),
                thursday = Hours("07:00", "15:00"),
                friday = Hours("07:00", "15:00"),
                saturday = Hours("07:00", "15:00"),
              ),
            ),
          ),
        ),
//        RestaurantWithSchedule(
//          Location.Dogwood,
//          BusSchedule("03:00", "03:01", 1.minutes),
//          PhoneNumber("970-349-6338", "Order!"),
//          Website.global("https://thedogwoodcb.wordpress.com/"),
//          Website.facebookPage(
//            "https://www.facebook.com/thedogwoodcb/",
//          ),
//        ),
        RestaurantWithSchedule(
          Location.GeneralStore,
          PhoneNumber("970-349-2783", "Order!"),
          Website.global("https://www.cbsouthgeneralstore.com/"),
          Website.facebookPage(
            "https://www.facebook.com/cbsouthgeneralstore/",
          ),
          StandardSchedule(
            deliveryHours = None,
            carryOutHours = Some(
              HoursOfOperation("11:00", "19:00"),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location.McGills,
          PhoneNumber("970-349-5240", "Order!"),
          Website.global("https://www.mcgillscrestedbutte.com/"),
          Website.facebookPage(
            "https://www.facebook.com/pages/McGills-At-Crested-Butte/119847854694618",
          ),
          StandardSchedule(
            deliveryHours = None,
            carryOutHours = Some(
              HoursOfOperation("09:00", "14:00"),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location.Montanyas,
          PhoneNumber("970-799-3206", "Order!"),
          Website.global("https://www.montanyarum.com/shop"),
          Website.facebookPage(
            "https://www.facebook.com/MontanyaDistillers/",
          ),
          AdvanceOrdersOnly(
            "Order by 4 pm on Thursday and your order will be ready for pick up on Friday between 4 - 7 pm.",
          ),
        ),
        RestaurantWithSchedule(
          Location.OctopusCoffee,
          PhoneNumber("970-312-5394", "Order!"),
          Website.global("https://www.octopuscoffeecb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/octopuscoffeecb/",
          ),
          StandardSchedule(
            deliveryHours = Some(
              HoursOfOperation(
                sunday = Hours("10:00", "15:00"),
                monday = Hours("10:00", "15:00"),
                tuesday = ClosedAllDay,
                wednesday = ClosedAllDay,
                thursday = ClosedAllDay,
                friday = Hours("10:00", "15:00"),
                saturday = Hours("10:00", "15:00"),
              ),
            ),
            carryOutHours = Some(
              HoursOfOperation(
                sunday = Hours("10:00", "15:00"),
                monday = Hours("10:00", "15:00"),
                tuesday = ClosedAllDay,
                wednesday = ClosedAllDay,
                thursday = ClosedAllDay,
                friday = Hours("10:00", "15:00"),
                saturday = Hours("10:00", "15:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location.Pitas,
          PhoneNumber("970-349-0897", "Order!"),
          Website.global("http://pitasinparadise.com/"),
          Website.facebookPage(
            "https://www.facebook.com/PitasInParadise/",
          ),
          StandardSchedule(
            deliveryHours = None,
            carryOutHours = Some(
              HoursOfOperation("12:00", "18:00"),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location.SecretStash,
          PhoneNumber("970-209-5159", "Order!"),
          Website.global("http://www.secretstash.com/"),
          Website.facebookPage(
            "https://www.facebook.com/TheSecretStashPizza",
          ),
          StandardSchedule(
            deliveryHours = Some(
              HoursOfOperation(
                sunday = Hours("16:30", "19:30"),
                monday = ClosedAllDay,
                tuesday = ClosedAllDay,
                wednesday = ClosedAllDay,
                thursday = Hours("16:30", "19:30"),
                friday = Hours("16:30", "19:30"),
                saturday = Hours("16:30", "19:30"),
              ),
            ),
            carryOutHours = Some(
              HoursOfOperation(
                sunday = Hours("16:30", "19:30"),
                monday = ClosedAllDay,
                tuesday = ClosedAllDay,
                wednesday = ClosedAllDay,
                thursday = Hours("16:30", "19:30"),
                friday = Hours("16:30", "19:30"),
                saturday = Hours("16:30", "19:30"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location.Slogar,
          PhoneNumber("970-349-5765", "Order!"),
          Website.global("https://www.slogarcb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/slogarqueen/",
          ),
          AdvanceOrdersOnly(
            """Place order Thursday-Friday BY 6 on FRIDAYS!
              |We will call/text you with confirmation on SATURDAY
              |to tell you what time to pick up your order on SUNDAY.""".stripMargin,
          ),
        ),
        RestaurantWithSchedule(
          Location.SoupCon,
          PhoneNumber("970-349-5448", "Order!"),
          Website.global("https://www.soupconcb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/Soupconcb",
          ),
          AdvanceOrdersOnly(
            "Order by 5:00PM Friday. Pickup between 5:00 and 7:30 Saturday.",
          ),
        ),
        RestaurantWithSchedule(
          Location.Tullys,
          PhoneNumber("970-349-2444", "Order!"),
          Website.global("https://www.tullyscbsouth.com/"),
          Website.facebookPage(
            "https://www.facebook.com/tullys456/",
          ),
          StandardSchedule(
            deliveryHours = None,
            carryOutHours = Some(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = ClosedAllDay,
                tuesday = Hours("16:00", "21:00"),
                wednesday = Hours("16:00", "21:00"),
                thursday = Hours("16:00", "21:00"),
                friday = Hours("16:00", "21:00"),
                saturday = Hours("16:00", "21:00"),
              ),
            ),
          ),
        ),
      ),
    ) {}
