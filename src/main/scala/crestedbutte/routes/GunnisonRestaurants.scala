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
        RestaurantWithSchedule(
          Location("Gunnisack"),
          PhoneNumber("970-641-5445"),
          Website.global("https://www.thegunnisack.com/"),
          Website.facebookPage(
            "https://www.facebook.com/thegunnisack/",
          ),
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
        RestaurantWithSchedule(
          Location("Gunnison Vitamin & Health Deli"), // TODO Might need to shrink the name
          PhoneNumber("970-641-5928"),
          Website.global("https://gunnisonvitamin.net/"),
          Website.facebookPage(
            "https://www.facebook.com/gunnisonvitamin/",
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = ClosedAllDay,
                monday = Hours("08:00", "15:00"),
                tuesday = Hours("08:00", "15:00"),
                wednesday = Hours("08:00", "15:00"),
                thursday = Hours("08:00", "15:00"),
                friday = Hours("08:00", "15:00"),
                saturday = Hours("08:00", "15:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("High Alpine Brewing Company"), // TODO Might need to shrink the name
          PhoneNumber("970-642-4500"),
          Website.global("http://highalpinebrewing.com/"),
          Website.facebookPage(
            "https://www.facebook.com/highalpinebrewingcompany/",
          ),
          StandardSchedule.carryOutAndDelivery(
            HoursOfOperation(
              sunday = Hours("16:00", "20:00"),
              monday = ClosedAllDay,
              tuesday = ClosedAllDay,
              wednesday = Hours("16:00", "20:00"),
              thursday = Hours("16:00", "20:00"),
              friday = Hours("16:00", "20:00"),
              saturday = Hours("16:00", "20:00"),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Mario's Pizza"),
          PhoneNumber("970-641-1374"),
          Website.global("http://mariosgunnison.com/"),
          Website.facebookPage(
            "https://www.facebook.com/Marios-Pizza-Pasta-113554522011947/",
          ),
          StandardSchedule.carryOutAndDelivery(
            HoursOfOperation(
              sunday = ClosedAllDay,
              monday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              tuesday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              wednesday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              thursday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              friday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              saturday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("McDonald's"),
          PhoneNumber("970-641-5050"),
          Website.global(
            "https://www.mcdonalds.com/us/en-us/location/co/gunnison/800-e-tomichi/6315.html",
          ),
          Website.facebookPage(
            "https://www.facebook.com/McDonalds800ETomichi/",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation.everyDay("05:00", "23:00"),
          ),
        ),
        RestaurantWithSchedule(
          Location("Mocha's Drive Thru"),
          PhoneNumber("970-641-2006"),
          Website.global(
            "https://mochascoffeehouse.com/",
          ),
          Website.facebookPage(
            "https://www.facebook.com/Mochas-Coffeehouse-Bakery-144796784110/",
          ),
          StandardSchedule(
            deliveryHours =
              HoursOfOperation.everyDay("09:00", "14:00"),
            carryOutHours =
              HoursOfOperation.everyDay("06:30", "14:00"),
          ),
        ),
        RestaurantWithSchedule(
          Location("Pie Zans"),
          PhoneNumber("970-641-5255"),
          Website.global("https://www.piezanspizzeria.com/"),
          Website.facebookPage(
            "https://www.facebook.com/piezanspizzeria/",
          ),
          StandardSchedule.carryOutAndDelivery(
            HoursOfOperation.everyDay("11:00", "9:00"),
          ),
        ),
        RestaurantWithSchedule(
          Location("Powerstop"),
          PhoneNumber("970-641-2328"),
          Website.global("http://www.the-powerstop.com/"),
          Website.facebookPage(
            "https://www.facebook.com/PowerstopBarAndGrill/",
          ),
          StandardSchedule.carryOutAndDelivery(
            HoursOfOperation.everyDay("06:00", "23:00"),
          ),
        ),
        RestaurantWithSchedule(
          Location("Sherpa Cafe"),
          PhoneNumber("970-641-7480"),
          Website.global("https://www.sherpascafe.com/"),
          Website.facebookPage(
            "https://www.facebook.com/sherpacafegunnison/",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation
              .everyDay("17:00", "20:00"), // TODO Confirm days
          ),
        ),
        RestaurantWithSchedule(
          Location("Taco Bell"),
          ExternalActionCollection(
            CallLocation(PhoneNumber("970-641-0414")),
            Seq(
              VisitFacebookPage(
                Website.facebookPage(
                  "https://www.facebook.com/Taco-Bell-319798508053989/",
                ),
              ),
            ),
          ),
          Some(
            StandardSchedule.carryOutOnly(
              HoursOfOperation(
                sunday = Hours("10:00", "21:00"),
                monday = Hours("10:00", "21:00"),
                tuesday = Hours("10:00", "21:00"),
                wednesday = Hours("10:00", "21:00"),
                thursday = Hours("10:00", "21:00"),
                friday = Hours("10:00", "22:00"),
                saturday = Hours("9:00", "22:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Taco Cat Taco Cart"),
          PhoneNumber("970-209-6707"),
          Website.global("https://www.tacocattacocart.com/"),
          Website.facebookPage(
            "https://www.facebook.com/tacocattacocart/",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation(
              sunday = ClosedAllDay,
              monday = ClosedAllDay,
              tuesday = ClosedAllDay,
              wednesday = Hours("12:00", "14:00"),
              thursday = Hours("12:00", "14:00"),
              friday = Hours("12:00", "14:00"),
              saturday = ClosedAllDay,
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("The Dive Pub"),
          PhoneNumber("970-641-1375"),
          Website.global("https://www.thedivegunnison.com/"),
          Website.facebookPage(
            "https://www.facebook.com/thedivegunnison/",
          ),
          StandardSchedule.carryOutAndDelivery(
            HoursOfOperation(
              sunday = ClosedAllDay,
              monday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              tuesday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              wednesday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              thursday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              friday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
              saturday = HoursGrouping(
                Hours("12:00", "14:00", "16:00", "18:00"),
              ),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("Tributary Coffee Roaster"),
          ExternalActionCollection(
            VisitHomePage(
              Website.global("https://www.tributarycoffee.com/"),
            ),
            Seq(
              VisitFacebookPage(
                Website.facebookPage(
                  "https://www.facebook.com/tributarycoffeeroasters/",
                ),
              ),
            ),
          ),
          Some(
            CompletelyUnstructedOperation(
              "Delivery only. Please place orders through website.",
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("W Cafe"),
          PhoneNumber("970-641-1744"),
          Website.global("http://thewcafe.com/"),
          Website.facebookPage(
            "https://www.facebook.com/wcafegunnison/",
          ),
          StandardSchedule.carryOutOnly(
            HoursOfOperation(
              sunday = Hours("08:00", "13:00"),
              monday = Hours("08:00", "13:00"),
              tuesday = Hours("08:00", "13:00"),
              wednesday = ClosedAllDay,
              thursday = Hours("08:00", "13:00"),
              friday = Hours("08:00", "13:00"),
              saturday = Hours("08:00", "13:00"),
            ),
          ),
        ),
        RestaurantWithSchedule(
          Location("The Burrito Kitchen of Gunnison"),
          ExternalActionCollection(
            CallLocation(
              PhoneNumber("970-641-9687"),
            ),
          ),
          Some(
            CompletelyUnstructedOperation(
              "Frozen and ready to cook at your home. Available at Mocha's Drive Thru or delivered to your home. $30/6 pack.",
            ),
          ),
        ),
      ),
    )
