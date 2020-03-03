package crestedbutte.routes

import crestedbutte.time.BusDuration.toBusDuration
import crestedbutte.{BusSchedule, BusScheduleAtStop, Location}

object RtaNorthbound {

  object Express {

    val gunnisonSchoolsDepartures: BusScheduleAtStop =
      BusScheduleAtStop(
        Location.GunnisonCommunitySchools,
        BusSchedule(
          "06:30",
          "06:45",
          "07:00",
          "07:30",
          "08:00",
          "08:30"
        )
      )

    val eleventhAndVirgina: BusScheduleAtStop =
      gunnisonSchoolsDepartures
        .delayedBy(2.minutes)
        .at(Location.EleventhAndVirginia)

    val safewayDepartures: BusScheduleAtStop =
      eleventhAndVirgina
        .delayedBy(4.minutes)
        .at(Location.Safeway)

    val tellerAndHighwayFifty: BusScheduleAtStop =
      safewayDepartures
        .delayedBy(2.minutes)
        .at(Location.TellerAndHighwayFifty)

    val western: BusScheduleAtStop =
      tellerAndHighwayFifty
        .delayedBy(2.minutes)
        .at(Location.Western)

    val denverAndHighwayOneThirtyFive: BusScheduleAtStop =
      western
        .delayedBy(3.minutes)
        .at(Location.DenverAndHighwayOneThirtyFive)

    val spencerAndHighwayOneThirtyFive: BusScheduleAtStop =
      denverAndHighwayOneThirtyFive
        .delayedBy(2.minutes)
        .at(Location.SpencerAndHighwayOneThirtyFive)

  }

  val gunnisonSchoolsDepartures: BusScheduleAtStop =
    BusScheduleAtStop(
      Location.GunnisonCommunitySchools,
      BusSchedule(
        "05:30",
        "06:00",
        "09:00",
        "09:30",
        "10:00",
        "10:30",
        "11:05",
        "11:35",
        "12:05",
        "13:00",
        "13:30",
        "14:15",
        "15:05",
        "15:30",
        "16:00",
        "16:30",
        "17:00",
        "18:00",
        "19:05",
        "20:05",
        "21:10",
        "22:10"
      )
    )

  val eleventhAndVirgina: BusScheduleAtStop =
    gunnisonSchoolsDepartures
      .delayedBy(2.minutes)
      .at(Location.EleventhAndVirginia)

  val safewayDepartures: BusScheduleAtStop =
    eleventhAndVirgina
      .delayedBy(4.minutes)
      .at(Location.Safeway)

  val tellerAndHighwayFifty: BusScheduleAtStop =
    safewayDepartures
      .delayedBy(2.minutes)
      .at(Location.TellerAndHighwayFifty)

  val western: BusScheduleAtStop =
    tellerAndHighwayFifty
      .delayedBy(2.minutes)
      .at(Location.Western)

  val denverAndHighwayOneThirtyFive: BusScheduleAtStop =
    western
      .delayedBy(3.minutes)
      .at(Location.DenverAndHighwayOneThirtyFive)

  val spencerAndHighwayOneThirtyFive: BusScheduleAtStop =
    denverAndHighwayOneThirtyFive
      .delayedBy(2.minutes)
      .at(Location.SpencerAndHighwayOneThirtyFive)

  val stops: Seq[BusScheduleAtStop] = Seq(
    BusScheduleAtStop.combine(gunnisonSchoolsDepartures,
                              Express.gunnisonSchoolsDepartures),
    BusScheduleAtStop.combine(eleventhAndVirgina,
                              Express.eleventhAndVirgina),
    BusScheduleAtStop.combine(safewayDepartures,
                              Express.safewayDepartures),
    BusScheduleAtStop.combine(tellerAndHighwayFifty,
                              Express.tellerAndHighwayFifty),
    BusScheduleAtStop.combine(western, Express.western),
    BusScheduleAtStop.combine(denverAndHighwayOneThirtyFive,
                              Express.denverAndHighwayOneThirtyFive),
    BusScheduleAtStop.combine(spencerAndHighwayOneThirtyFive,
                              Express.spencerAndHighwayOneThirtyFive)
  )

}
