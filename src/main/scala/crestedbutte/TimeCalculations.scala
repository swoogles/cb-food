package crestedbutte

import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}

import crestedbutte.time.{
  BusTime,
  ClosedForTheDay,
  DailyHours,
  DailyHoursSegment,
  DailyInfo,
}
import zio.ZIO
import zio.clock.Clock

object TimeCalculations {

  def nextBusArrivalTime(timesAtStop: Seq[BusTime],
                         now: BusTime): Option[BusTime] =
    timesAtStop
      .find(stopTime => BusTime.catchableBus(now, stopTime))
      .filter(_ => now.isLikelyEarlyMorningRatherThanLateNight)

  def currentStatus(dailyInfo: DailyInfo,
                    now: BusTime): RestaurantStatus =
    dailyInfo match {
      case ClosedForTheDay(dayOfWeek) => Closed
      case DailyHours(
          dailyHoursSegments: Seq[DailyHoursSegment],
          dayOfWeek,
          ) => // TODO This will need to handle multiple time segments.
        if (dailyHoursSegments
              .map(statusForCurrentSegment(_, now))
              .exists(
                statusForCurrentSegment =>
                  statusForCurrentSegment.equals(Open),
              )) Open
        else Closed
    }

  def statusForCurrentSegment(hoursSegment: DailyHoursSegment,
                              now: BusTime): RestaurantStatus =
    hoursSegment match {
      case DailyHoursSegment(open, close) => // TODO This will need to handle multiple time segments.
        if (now.isBefore(open) && now.between(open).toMinutes > 30)
          OpeningSoon
        else if (now.isAfterOrEqualTo(open) && now.isBefore(close))
          if (now.between(close).toMinutes < 20)
            ClosingSoon
          else
            Open
        else
          Closed
    }

  def getUpcomingArrivalInfo(
    restaurant: RestaurantWithSchedule,
    now: Instant,
  ): RestaurantWithStatus =
    restaurant.businessDetails match {
      case Some(details) =>
        details match {
          case AdvanceOrdersOnly(instructions) =>
            RestaurantWithStatus(restaurantWithSchedule = restaurant,
                                 carryOutStatus = Unknown,
                                 deliveryStatus = Unknown)
          case CompletelyUnstructedOperation(instructions) =>
            RestaurantWithStatus(restaurantWithSchedule = restaurant,
                                 carryOutStatus = Unknown,
                                 deliveryStatus = Unknown)

          case StandardSchedule(deliveryHours, carryOutHours) => {
            val localDateTime =
              LocalDateTime.ofInstant(
                now,
                ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-7)),
              )
            val localTime = new BusTime(localDateTime.toLocalTime)
            val deliveryStatus =
              deliveryHours
                .map(
                  hoursOfOperation =>
                    currentStatus(hoursOfOperation.dailyInfoFor(
                                    localDateTime.getDayOfWeek,
                                  ),
                                  localTime),
                )
                .getOrElse(Unknown)
            val carryOutStatus =
              carryOutHours
                .map(
                  hoursOfOperation =>
                    currentStatus(hoursOfOperation.dailyInfoFor(
                                    localDateTime.getDayOfWeek,
                                  ),
                                  localTime),
                )
                .getOrElse(Unknown)
            RestaurantWithStatus(restaurantWithSchedule = restaurant,
                                 carryOutStatus = carryOutStatus,
                                 deliveryStatus = deliveryStatus)

          }

        }
      case None =>
        RestaurantWithStatus(restaurantWithSchedule = restaurant,
                             carryOutStatus = Unknown,
                             deliveryStatus = Unknown)
    }

  def calculateUpcomingArrivalAtAllStops(
    now: Instant,
    busRoute: NamedRestaurantGroup,
  ): Seq[RestaurantWithStatus] =
    busRoute.restaurantGroup.allRestaurants.map(
      (scheduleAtStop: RestaurantWithSchedule) =>
        getUpcomingArrivalInfo(scheduleAtStop, now),
    )

  val now: ZIO[Clock, Nothing, Instant] =
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
    } yield {
      now.toInstant
    }

  def getUpComingArrivals(
    namedRestaurantgroup: NamedRestaurantGroup,
  ): ZIO[Clock, Nothing, Seq[RestaurantWithStatus]] =
    for {
      clockProper <- ZIO.environment[Clock]
      now         <- clockProper.clock.currentDateTime
      nowInstant = now.toInstant // TODO This is where I get my time
    } yield {
      TimeCalculations.calculateUpcomingArrivalAtAllStops(
        nowInstant,
        namedRestaurantgroup,
      )
    }

}
