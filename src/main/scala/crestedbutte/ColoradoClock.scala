package crestedbutte

import java.time.{Instant, OffsetDateTime, ZoneId, ZonedDateTime}
import java.util.concurrent.TimeUnit

import zio.clock.Clock
import zio.clock.Clock.Service
import zio.duration.Duration
import zio.scheduler.SchedulerLive
import zio.{IO, UIO, ZIO}

object ColoradoClock {

  trait Live extends SchedulerLive with Clock {

    val clock: Service[Any] = new Service[Any] {

      def currentTime(unit: TimeUnit): UIO[Long] =
        IO.effectTotal(System.currentTimeMillis)
          .map(l => unit.convert(l, TimeUnit.MILLISECONDS))

      val nanoTime: UIO[Long] = IO.effectTotal(System.nanoTime)

//      def sleep(duration: Duration): UIO[Unit] =
//        UIO.unit
      def sleep(duration: Duration): UIO[Unit] =
        scheduler.scheduler.flatMap(
          scheduler =>
            ZIO.effectAsyncInterrupt[Any, Nothing, Unit] { k =>
              val canceler = scheduler
                .schedule(() => k(ZIO.unit), duration)

              Left(ZIO.effectTotal(canceler()))
            },
        )

      def currentDateTime: ZIO[Any, Nothing, OffsetDateTime] =
        for {
          millis <- currentTime(TimeUnit.MILLISECONDS)
          _ <- ZIO.succeed {
            println("Getting a hard-coded Colorado timezone")
          }
          zone <- ZIO.effectTotal(ZoneId.of("America/Denver"))
        } yield OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis),
                                         zone)

    }
  }
  object Live extends Live
}
