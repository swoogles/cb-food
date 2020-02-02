//scalaVersion := "2.12.10"
scalaVersion := "2.13.1"

enablePlugins(ScalaJSPlugin)

enablePlugins(TzdbPlugin)

libraryDependencies ++= Seq(
  "dev.zio" %%% "zio" % "1.0.0-RC17",
  "com.lihaoyi" %%% "scalatags" % "0.8.2",
  "org.scala-js" %%% "scalajs-dom" % "0.9.7",
  "io.github.cquiroz" %%% "scala-java-time" % "2.0.0-RC3",
//  "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.0.0-RC3_2019a",
//    libraryDependencies += "io.github.cquiroz" % "scala-java-time_2.13" % "2.0.0-RC3"
  "dev.zio" %%% "zio-test"     % "1.0.0-RC17" % "test",
  "dev.zio" %%% "zio-test-sbt" % "1.0.0-RC17" % "test",
  "com.lihaoyi" %%% "pprint" % "0.5.6",
)

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

lazy val cbBuild = taskKey[Unit]("Execute the shell script")

cbBuild := {
  (Compile/fastOptJS).value
  import scala.sys.process._
//  "ls ./target/scala-2.13" !
  (Process("mkdir ./src/main/resources/compiledJavascript") #||
    Process("cp ./target/scala-2.13/cb-bus-fastopt.js src/main/resources/compiledJavascript/") #&&
    Process("cp ./target/scala-2.13/cb-bus-fastopt.js.map src/main/resources/compiledJavascript/") #&&
    Process("cp ./target/scala-2.13/cb-bus-jsdeps.js src/main/resources/compiledJavascript/"))!
}

lazy val cbPublish = taskKey[Unit]("Build the files for a real deploment")
cbPublish := {
  (Compile/fullOptJS).value
  (Compile/scalafmt).value
  import scala.sys.process._
  //  "ls ./target/scala-2.13" !
  (Process("mkdir ./src/main/resources/compiledJavascript") #||
    Process("cp ./target/scala-2.13/cb-bus-opt.js src/main/resources/compiledJavascript/"))!
}

zonesFilter := {(z: String) => z == "America/Denver" || z == "America/Mountain"}
