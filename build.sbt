
name := "avrotool"
organization := "org.exaspace"
scalaVersion := "2.12.8"
scalacOptions := Seq("-deprecation", "-unchecked")

val argParseLibs = Seq(
  "org.rogach" %% "scallop" % "3.3.1",
)

val jsonLibs = Seq(
  "io.spray" %% "spray-json" % "1.3.5"
)

val loggingLibs = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
)

val avroLibs = Seq(
  "org.apache.avro" % "avro" % "1.9.0"
)

val httpLibs = Seq(
  "com.softwaremill.sttp" %% "core" % "1.5.19"
)

val testLibs = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8"
).map(_ % Test)

libraryDependencies ++= argParseLibs ++ jsonLibs ++ loggingLibs ++ avroLibs ++ httpLibs ++ testLibs

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)
