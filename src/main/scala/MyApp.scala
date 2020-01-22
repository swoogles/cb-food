import zio.{App, ZIO}
import zio.console._
import zio.clock._

object MyApp extends App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    val logic = for {
      _ <- putStr("name: ")
      name <- getStrLn
      now <- currentDateTime
      _ <- putStrLn(s"hello, $name - $now")
    } yield ()

    logic.fold(_ => 1, _ => 0)
  }
}
