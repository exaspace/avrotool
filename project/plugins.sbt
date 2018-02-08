
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.2")

/*
sbt pack works much better out of the box for command line apps due to a less borked start script
 */
addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.9.1")
