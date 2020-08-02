//scalaVersion := "2.12.10"
scalaVersion := "2.13.1"
version := "0.2"

enablePlugins(ScalaJSPlugin)

enablePlugins(TzdbPlugin)

resolvers += "jitpack" at "https://jitpack.io"
resolvers += Resolver.url("typesafe", url("http://repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns)
resolvers += Resolver.githubPackages("swoogles", "BulmaScala")

val zioVersion = "1.0.0-RC21-2"

libraryDependencies ++= Seq(
  "com.billding" %%% "bulmalibrary" % "0.2.19",
  "com.billding" %%% "scalajsziolibrary" % "0.0.13",
  "com.billding" %%% "brieftime" % "0.0.16",
  "com.lihaoyi" %%% "scalatags" % "0.8.6",
  "org.scala-js" %%% "scalajs-dom" % "1.0.0",
  "dev.zio" %%% "zio" % zioVersion,
  "dev.zio" %%% "zio-streams" % zioVersion,
//  "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.0.0-RC3_2019a",
//    libraryDependencies += "io.github.cquiroz" % "scala-java-time_2.13" % "2.0.0-RC3"
  "com.lihaoyi" %%% "pprint" % "0.5.9",
"dev.zio" %%% "zio-test"     % zioVersion % "test",
"dev.zio" %%% "zio-test-sbt" % zioVersion % "test",
)

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

lazy val cbBuild = taskKey[Unit]("Execute the shell script")

cbBuild := {
  (Compile/fastOptJS).value
  (Compile/scalafmt).value
  import scala.sys.process._
//  "ls ./target/scala-2.13" !
  (Process("mkdir ./src/main/resources/compiledJavascript") #||
    Process("cp ./target/scala-2.13/cb-food-fastopt.js src/main/resources/compiledJavascript/") #&&
    Process("cp ./target/scala-2.13/cb-food-fastopt.js.map src/main/resources/compiledJavascript/") #&&
    Process("cp sw/target/scala-2.12/sw-opt.js src/main/resources/") #&&
    Process("cp sw/target/scala-2.12/sw-opt.js.map src/main/resources/") #&&
    Process("cp ./target/scala-2.13/cb-food-jsdeps.js src/main/resources/compiledJavascript/"))!
}

lazy val cbPublish = taskKey[Unit]("Build the files for a real deploment")
cbPublish := {
  (Compile/fullOptJS).value
  (Compile/scalafmt).value
  (sw/Compile/fullOptJS).value
  import scala.sys.process._
  //  "ls ./target/scala-2.13" !
  (Process("mkdir ./src/main/resources/compiledJavascript") ###
    Process("cp ./target/scala-2.13/cb-food-opt.js src/main/resources/compiledJavascript/") ###
    Process("cp ./target/scala-2.13/cb-food-opt.js.map src/main/resources/compiledJavascript/") ###
    Process("cp sw/target/scala-2.12/sw-opt.js src/main/resources/") ###
    Process("cp sw/target/scala-2.12/sw-opt.js.map src/main/resources/"))!
}

zonesFilter := {(z: String) => z == "America/Denver" || z == "America/Mountain"}

lazy val sw = (project in file("sw"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.0.0"
    )
  )
