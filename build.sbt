

name := "avrotool"
organization := "org.exaspace"
scalaVersion := "2.12.16"
scalacOptions := Seq("-deprecation", "-unchecked")

val argParseLibs = Seq(
  "org.rogach" %% "scallop" % "4.1.0",
)

val jsonLibs = Seq(
  "io.spray" %% "spray-json" % "1.3.6"
)

val loggingLibs = Seq(
  "ch.qos.logback" % "logback-classic" % "1.4.5",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
)

val avroLibs = Seq(
  "org.apache.avro" % "avro" % "1.9.0"
)

val httpLibs = Seq(
  "com.softwaremill.sttp" %% "core" % "1.7.2"
)

val testLibs = Seq(
  "org.scalatest" %% "scalatest" % "3.0.9"
).map(_ % Test)

libraryDependencies ++= argParseLibs ++ jsonLibs ++ loggingLibs ++ avroLibs ++ httpLibs ++ testLibs

lazy val root = (project in file(".")).enablePlugins(PackPlugin)
