package crestedbutte.routes

import crestedbutte.time.{
  ClosedAllDay,
  Hours,
  HoursGrouping,
  HoursOfOperation,
}
import crestedbutte.{
  CallLocation,
  CompletelyUnstructedOperation,
  ExternalActionCollection,
  Location,
  PhoneNumber,
  RestaurantGroupName,
  RestaurantWithSchedule,
  StandardSchedule,
  VisitFacebookPage,
  VisitHomePage,
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
        RestaurantWithSchedule(
          Location("Anjeo"),
          ExternalActionCollection(
            CallLocation(PhoneNumber("970-641-1427")),
            Seq(
              VisitHomePage(
                Website.facebookPage(
                  "https://www.facebook.com/AnejoBistroBar/",
                ),
              ),
            ),
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = Hours("11:30", "20:00"),
                tuesday = Hours("11:30", "20:00"),
                wednesday = Hours("11:30", "20:00"),
                thursday = Hours("11:30", "20:00"),
                friday = Hours("11:30", "20:00"),
                saturday = Hours("11:30", "20:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Arby's"),
          PhoneNumber("970-641-8851"),
          Website.global(
            "https://locations.arbys.com/co/gunnison/800-e-tomichi-ave-temp.html",
          ),
          Website.facebookPage(
            "https://www.facebook.com/Arbys-468185087012524/",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation.everyDay("10:00", "20:00"),
          ),
        ),
        RestaurantWithSchedule(
          Location("Blackstock Bistro"),
          ExternalActionCollection(
            VisitHomePage(
              Website.onlineOrder(
                "https://us.orderspoon.com/blackstock-bistro",
              ),
            ),
            Seq(
              VisitHomePage(
                Website.global("http://blackstockbistro.com/"),
              ),
              // TODO Ask them which actions they prefer
//              VisitFacebookPage(
//                Website.facebookPage(
//                  "https://www.facebook.com/Blackstock-Bistro-1440692279549356/",
//                ),
//              ),
              CallLocation(PhoneNumber("970-641-4394")),
            ),
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = HoursGrouping(
                  Hours("12:00", "14:30", "17:00", "19:30"),
                ),
                monday = HoursGrouping(
                  Hours("12:00", "14:30", "17:00", "19:30"),
                ),
                tuesday = ClosedAllDay,
                wednesday = HoursGrouping(
                  Hours("12:00", "14:30", "17:00", "19:30"),
                ),
                thursday = HoursGrouping(
                  Hours("12:00", "14:30", "17:00", "19:30"),
                ),
                friday = HoursGrouping(
                  Hours("12:00", "14:30", "17:00", "19:30"),
                ),
                saturday = HoursGrouping(
                  Hours("12:00", "14:30", "17:00", "19:30"),
                ),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Buckel Family Wine"),
          PhoneNumber("970-349-2071"),
          Website.global("https://www.buckelfamilywine.com/"),
          Website.facebookPage(
            "https://www.facebook.com/BuckelFamily/",
          ),
          Some(
            CompletelyUnstructedOperation(
              "Call or email sales@buckelfamilywine.com to order wine to be picked up at the winery or for Thursday delivery.",
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Cafe Silvestri"),
          ExternalActionCollection(
            CallLocation(PhoneNumber("970-642-4550")), // TODO confirm number. It's either this or 970-641-4001
            Seq(
              VisitFacebookPage(
                Website.facebookPage(
                  "https://www.facebook.com/pages/Cafe-Silvestri/176065582455513",
                ),
              ),
            ),
          ),
          Some(
            CompletelyUnstructedOperation(
              "Carry-out Monday-Friday 10:30AM-2:00PM & 4:30PM-7:00PM",
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Crumb de la Crumb"),
          PhoneNumber("970-596-8128"),
          Website.global("https://crumbdelacrumbbake.com/"),
          Website.facebookPage(
            "https://www.facebook.com/Crumb-de-la-Crumb-LLC-203085957157606/",
          ),
          Some(
            CompletelyUnstructedOperation(
              "Advance Orders only. Complimentary delivery Monday-Friday.",
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Dominos"),
          PhoneNumber("970-707-4199"),
          Website.global(
            "https://pizza.dominos.com/colorado/gunnison/",
          ),
          Website.facebookPage(
            "https://www.facebook.com/DominosGunnisonCO/",
          ),
          StandardSchedule.carryOutAndDelivery(
            HoursOfOperation(
              sunday = Hours("10:00", "24:00"),
              monday = Hours("10:00", "24:00"),
              tuesday = Hours("10:00", "24:00"),
              wednesday = Hours("10:00", "24:00"),
              thursday = Hours("10:00", "24:00"),
              friday = Hours("10:00", "01:00"), // TODO Really need to render 1AM properly for this to make sense
              saturday = Hours("10:00", "01:00"),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("El Paraiso"),
          ExternalActionCollection(
            CallLocation(PhoneNumber("970-641-4957")),
            Seq(
              VisitFacebookPage(
                Website.facebookPage(
                  "https://www.facebook.com/pages/El-Paraiso/111826328853249?rf=1494752060823593",
                ),
              ),
            ),
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation.apply(
                sunday = Hours("11:00", "20:00"),
                monday = Hours("11:00", "20:00"),
                tuesday = Hours("11:00", "20:00"),
                wednesday = Hours("11:00", "20:00"),
                thursday = Hours("11:00", "20:00"),
                friday = Hours("11:00", "21:00"),
                saturday = Hours("11:00", "21:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Firebrand"),
          ExternalActionCollection(
            CallLocation(PhoneNumber("970-641-6266")),
            Seq(
              VisitFacebookPage(
                Website.facebookPage(
                  "https://www.facebook.com/FirebrandDelicatessen/",
                ),
              ),
            ),
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = ClosedAllDay,
                tuesday = ClosedAllDay,
                wednesday = Hours("08:00", "13:00"),
                thursday = Hours("08:00", "13:00"),
                friday = Hours("08:00", "13:00"),
                saturday = Hours("08:00", "13:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Garlic Mikes"),
          PhoneNumber("970-641-2493"),
          Website.global("https://garlicmikes.com/"),
          Website.facebookPage(
            "https://www.facebook.com/garlicmikesrestaurant/",
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation.apply(
                sunday = ClosedAllDay,
                monday = ClosedAllDay,
                tuesday = Hours("17:00", "19:00"),
                wednesday = Hours("17:00", "19:00"),
                thursday = Hours("17:00", "19:00"),
                friday = Hours("17:00", "19:00"),
                saturday = Hours("17:00", "19:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Gunnisack"),
          PhoneNumber("970-641-5445"),
          Website.global("https://www.thegunnisack.com/"),
          Website.facebookPage(
            "https://www.facebook.com/thegunnisack/",
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = HoursGrouping(
                  Hours("11:30", "14:00", "17:00", "20:00"),
                ),
                tuesday = HoursGrouping(
                  Hours("11:30", "14:00", "17:00", "20:00"),
                ),
                wednesday = HoursGrouping(
                  Hours("11:30", "14:00", "17:00", "20:00"),
                ),
                thursday = HoursGrouping(
                  Hours("11:30", "14:00", "17:00", "20:00"),
                ),
                friday = HoursGrouping(
                  Hours("11:30", "14:00", "17:00", "20:00"),
                ),
                saturday = HoursGrouping(
                  Hours("11:30", "14:00", "17:00", "20:00"),
                ),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Gunnison Coffee Company"),
          ExternalActionCollection(
            VisitFacebookPage(
              Website.facebookPage(
                "https://www.facebook.com/gunnisoncoffeecompany/",
              ),
            ),
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = Hours("08:00", "12:00"),
                monday = Hours("06:00", "12:00"),
                tuesday = Hours("06:00", "12:00"),
                wednesday = Hours("06:00", "12:00"),
                thursday = Hours("06:00", "12:00"),
                friday = Hours("06:00", "12:00"),
                saturday = Hours("08:00", "12:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Gunnison Pizza Company"),
          PhoneNumber("970-641-1110"),
          Website.global("https://www.gunnisonpizzaco.com/"),
          Website.facebookPage(
            "https://www.facebook.com/gunnisonpizza/",
          ),
          Some(
            StandardSchedule.carryOutAndDelivery(
              HoursOfOperation(
                sunday = Hours("17:00", "20:00"),
                monday = ClosedAllDay,
                tuesday = Hours("17:00", "20:00"),
                wednesday = Hours("17:00", "20:00"),
                thursday = Hours("17:00", "20:00"),
                friday = Hours("17:00", "20:00"),
                saturday = Hours("17:00", "20:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Gunnison Vitamin & Health Food"), // TODO Might need to shrink the name
          PhoneNumber("970-641-5928"),
          Website.global("https://gunnisonvitamin.net/"),
          Website.facebookPage(
            "https://www.facebook.com/gunnisonvitamin/",
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = Hours("08:00", "19:00"),
                tuesday = Hours("08:00", "19:00"),
                wednesday = Hours("08:00", "19:00"),
                thursday = Hours("08:00", "19:00"),
                friday = Hours("08:00", "19:00"),
                saturday = Hours("08:00", "19:00"),
              ),
            ),
          ),
        ),
      ),
    )
