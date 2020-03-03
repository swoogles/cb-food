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

    val tallTexan: BusScheduleAtStop =
      spencerAndHighwayOneThirtyFive
        .delayedBy(2.minutes)
        .at(Location.TallTexan)

    val ohioCreek: BusScheduleAtStop =
      tallTexan
        .delayedBy(1.minutes)
        .at(Location.OhioCreek)

    val almont: BusScheduleAtStop =
      ohioCreek
        .delayedBy(7.minutes)
        .at(Location.Almont)

    val riverLand: BusScheduleAtStop =
      almont
        .delayedBy(16.minutes)
        .at(Location.Riverland)

    val brushCreek: BusScheduleAtStop =
      riverLand
        .delayedBy(1.minutes)
        .at(Location.BrushCreek)

    val riverBend: BusScheduleAtStop =
      brushCreek
        .delayedBy(1.minutes)
        .at(Location.Riverbend)

    val fourWay: BusScheduleAtStop =
      riverBend
        .delayedBy(3.minutes)
        .at(Location.FourWayUphill)

    val mtCbTransitCenter: BusScheduleAtStop =
      riverBend
        .delayedBy(10.minutes)
        .at(Location.MountaineerSquare)
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

  val tallTexan: BusScheduleAtStop =
    spencerAndHighwayOneThirtyFive
      .delayedBy(2.minutes)
      .at(Location.TallTexan)

  val ohioCreek: BusScheduleAtStop =
    tallTexan
      .delayedBy(1.minutes)
      .at(Location.OhioCreek)

  val almont: BusScheduleAtStop =
    ohioCreek
      .delayedBy(7.minutes)
      .at(Location.Almont)

  val cbSouth: BusScheduleAtStop =
    almont
      .delayedBy(17.minutes)
      .at(Location.CBSouth)

  val riverLand: BusScheduleAtStop =
    cbSouth
      .delayedBy(5.minutes)
      .at(Location.Riverland)

  val brushCreek: BusScheduleAtStop =
    riverLand
      .delayedBy(1.minutes)
      .at(Location.BrushCreek)

  val riverBend: BusScheduleAtStop =
    brushCreek
      .delayedBy(1.minutes)
      .at(Location.Riverbend)

  val fourWay: BusScheduleAtStop =
    riverBend
      .delayedBy(3.minutes)
      .at(Location.FourWayUphill)

  val mtCbTransitCenter: BusScheduleAtStop =
    riverBend
      .delayedBy(10.minutes)
      .at(Location.MountaineerSquare)

  // TODO When I turn a Seq[BusScheduleAtStop] into a real class, make an End-of-Line field.
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
                              Express.spencerAndHighwayOneThirtyFive),
    BusScheduleAtStop.combine(tallTexan, Express.tallTexan),
    BusScheduleAtStop.combine(ohioCreek, Express.ohioCreek),
    BusScheduleAtStop.combine(almont, Express.almont),
    cbSouth,
    BusScheduleAtStop.combine(riverLand, Express.riverLand),
    BusScheduleAtStop.combine(brushCreek, Express.brushCreek),
    BusScheduleAtStop.combine(riverBend, Express.riverBend),
    BusScheduleAtStop.combine(fourWay, Express.fourWay),
    BusScheduleAtStop.combine(mtCbTransitCenter,
                              Express.mtCbTransitCenter)
  )

}
