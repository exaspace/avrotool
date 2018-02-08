
val argParseLibs = Seq(
  "org.rogach" %% "scallop" % "3.1.0",
)

val jsonLibs = Seq(
  "io.spray" %% "spray-json" % "1.3.4"
)

val loggingLibs = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
)

val avroLibs = Seq(
  "org.apache.avro" % "avro" % "1.8.2"
)

val httpLibs = Seq(
  "com.softwaremill.sttp" %% "core" % "1.1.2"
)

val testLibs = Seq(
  "org.scalatest" %% "scalatest" % "3.0.4"
).map(_ % Test)


lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    inThisBuild(List(
      organization := "org.exaspace",
      scalaVersion := "2.12.4",
      version := "0.1.0-SNAPSHOT",
      scalacOptions := Seq("-deprecation", "-unchecked")
    )),
    name := "avrotool",
    libraryDependencies ++= argParseLibs ++ jsonLibs ++
        loggingLibs ++ avroLibs ++ httpLibs ++ testLibs
  )
