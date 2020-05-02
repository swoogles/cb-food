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
  CallLocation,
  ExternalActionCollection,
  Name,
  PhoneNumber,
  Restaurant,
  StandardSchedule,
  VisitFacebookPage,
  VisitHomePage,
  Website,
}

object CbRestaurantsAndSchedules
    extends RestaurantGroup(
      Name("Crested Butte Restaurants"),
      Seq(
        Restaurant(
          Name("The Brick Oven"),
          PhoneNumber("970-349-5044", "Order!"),
          Website.global("http://brickovencb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/BrickOvenCB/",
          ),
          StandardSchedule(
            deliveryHours =
              HoursOfOperation.everyDay("17:00", "20:00"),
            carryOutHours =
              HoursOfOperation.everyDay("11:00", "20:00"),
          ),
        ),
        Restaurant(
          Name("Bonez"),
          PhoneNumber("970-349-5118", "Order!"),
          Website.global("https://www.bonez.co/"),
          Website.facebookPage(
            "https://www.facebook.com/BONEZ-662144153840370/",
          ),
          StandardSchedule.carryOutAndDelivery(
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
        Restaurant(
          Name("Butte Bagels"),
          PhoneNumber("970-349-5630", "Order!"),
          Website.global("https://butte-bagels.com/"),
          Website.facebookPage(
            "https://www.facebook.com/pages/category/Bagel-Shop/Butte-Bagels-1225240390980501/",
          ),
          StandardSchedule.carryOutOnly(
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
//        RestaurantWithSchedule(
//          Location("Coal Creek Grill"),
//          BusSchedule("03:00", "03:01", 1.minutes),
//          PhoneNumber("970-349-6645", "Order!"),
//          Website.global("http://www.coalcreekgrill.com/"),
//          Website.facebookPage(
//            "https://www.facebook.com/CoalCreekGrillCB/",
//          ),
//        ),
        Restaurant(
          Name("The Divvy"),
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
        Restaurant(
          Name("Elk Prime"),
          ExternalActionCollection(
            VisitHomePage(
              Website.onlineOrder(
                "https://order.tbdine.com/elk-ave-prime-s/pickup",
              ),
            ),
            Seq(
              CallLocation(PhoneNumber("970-349-1221")),
              VisitHomePage(
                Website.global(
                  "https://elkaveprime.com/",
                ),
              ),
            ),
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = Hours("17:00", "19:30"),
                monday = Hours("17:00", "19:30"),
                tuesday = ClosedAllDay,
                wednesday = ClosedAllDay,
                thursday = Hours("17:00", "19:30"),
                friday = Hours("17:00", "19:30"),
                saturday = Hours("17:00", "19:30"),
              ),
            ),
          ),
        ),
        Restaurant(
          Name("Gas Cafe"),
          PhoneNumber("970-349-9656", "Order!"),
          Website.global("https://www.gascafe1stop.com/"),
          Website.facebookPage(
            "https://www.facebook.com/gascafeonestop/",
          ),
          StandardSchedule.carryOutOnly(
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
//        RestaurantWithSchedule(
//          Location("Dogwood"),
//          BusSchedule("03:00", "03:01", 1.minutes),
//          PhoneNumber("970-349-6338", "Order!"),
//          Website.global("https://thedogwoodcb.wordpress.com/"),
//          Website.facebookPage(
//            "https://www.facebook.com/thedogwoodcb/",
//          ),
//        ),
        Restaurant(
          Name("General Store"),
          PhoneNumber("970-349-2783", "Order!"),
          Website.global("https://www.cbsouthgeneralstore.com/"),
          Website.facebookPage(
            "https://www.facebook.com/cbsouthgeneralstore/",
          ),
          StandardSchedule.carryOutOnly(
            carryOutHours =
              HoursOfOperation.everyDay("11:00", "19:00"),
          ),
        ),
        Restaurant(
          Name("McGills"),
          PhoneNumber("970-349-5240", "Order!"),
          Website.global("https://www.mcgillscrestedbutte.com/"),
          Website.facebookPage(
            "https://www.facebook.com/pages/McGills-At-Crested-Butte/119847854694618",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation.everyDay("09:00", "14:00"),
          ),
        ),
        Restaurant(
          Name("Mikey's Pizza"),
          PhoneNumber("970-349-1110", "Order!"),
          Website.global("https://www.mikeyspizza.net/"),
          Website.facebookPage(
            "https://www.facebook.com/mikeyspizzacrestedbutte/",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation.everyDay("10:00", "19:00"),
          ),
        ),
        Restaurant(
          Name("Montanya"),
          PhoneNumber("970-799-3206", "Order!"),
          Website.global("https://www.montanyarum.com/shop"),
          Website.facebookPage(
            "https://www.facebook.com/MontanyaDistillers/",
          ),
          AdvanceOrdersOnly(
            "Order by 4 pm on Thursday and your order will be ready for pick up on Friday between 4 - 7 pm.",
          ),
        ),
        Restaurant(
          Name("Niky's"),
          ExternalActionCollection(
            VisitHomePage(
              Website.global("https://www.nikysminidonuts.com/"),
            ),
            List(
              VisitFacebookPage(
                Website.facebookPage(
                  "https://www.facebook.com/NikysMiniDonuts/",
                ),
              ),
              // TODO mention phone at all?
            ),
          ),
          Some(
            AdvanceOrdersOnly(
              """Niky's Mini Donuts has online ordering for delivery and take-out
                |on Saturday and Sunday each week from 10AM to 3PM.
                |Free delivery with $20 minimum order.""".stripMargin,
            ),
          ),
        ),
        Restaurant(
          Name("Octopus Coffee"),
          PhoneNumber("970-312-5394", "Order!"),
          Website.global("https://www.octopuscoffeecb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/octopuscoffeecb/",
          ),
          StandardSchedule.carryOutAndDelivery(
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
        Restaurant(
          Name("Pitas in Paradise"),
          PhoneNumber("970-349-0897", "Order!"),
          Website.global("http://pitasinparadise.com/"),
          Website.facebookPage(
            "https://www.facebook.com/PitasInParadise/",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation.everyDay("12:00", "18:00"),
          ),
        ),
        Restaurant(
          Name("Rumors Coffee & Tea House"),
          ExternalActionCollection(
            VisitHomePage( // TODO This is wrong now
              Website.onlineOrder(
                "https://rumorscoffee.square.site/",
              ),
            ),
            Seq(CallLocation(PhoneNumber("970-349-7545")),
                VisitFacebookPage(
                  Website.facebookPage(
                    "https://www.facebook.com/towniebooks/",
                  ),
                )),
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = Hours("08:00", "17:00"),
                tuesday = Hours("08:00", "17:00"),
                wednesday = Hours("08:00", "17:00"),
                thursday = Hours("08:00", "17:00"),
                friday = Hours("08:00", "17:00"),
                saturday = Hours("08:00", "17:00"),
              ),
            ),
          ),
        ),
        Restaurant(
          Name("Secret Stash"),
          PhoneNumber("970-209-5159", "Order!"),
          Website.global("http://www.secretstash.com/"),
          Website.facebookPage(
            "https://www.facebook.com/TheSecretStashPizza",
          ),
          StandardSchedule( // TODO create StandardSchedule.carryOutAndDelivery(
            deliveryHours = HoursOfOperation(
              sunday = Hours("16:30", "19:30"),
              monday = ClosedAllDay,
              tuesday = ClosedAllDay,
              wednesday = ClosedAllDay,
              thursday = Hours("16:30", "19:30"),
              friday = Hours("16:30", "19:30"),
              saturday = Hours("16:30", "19:30"),
            ),
            carryOutHours = HoursOfOperation(
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
        Restaurant(
          Name("Sherpa Cafe"),
          PhoneNumber("970-349-0443"),
          Website.global("https://www.sherpascafe.com/"),
          Website.facebookPage(
            "https://www.facebook.com/sherpacafecb",
          ),
          StandardSchedule.carryOutOnly(
            carryOutHours =
              HoursOfOperation.everyDay("17:00", "20:00"),
          ),
        ),
        Restaurant(
          Name("Slogar"),
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
        Restaurant(
          Name("Soupcon"),
          PhoneNumber("970-349-5448", "Order!"),
          Website.global("https://www.soupconcb.com/"),
          Website.facebookPage(
            "https://www.facebook.com/Soupconcb",
          ),
          AdvanceOrdersOnly(
            "Order by 5:00PM Friday. Pickup between 5:00 and 7:30 Saturday.",
          ),
        ),
        Restaurant(
          Name("Tully's"),
          PhoneNumber("970-349-2444", "Order!"),
          Website.global("https://www.tullyscbsouth.com/"),
          Website.facebookPage(
            "https://www.facebook.com/tullys456/",
          ),
          StandardSchedule.carryOutOnly(
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
    ) {}
