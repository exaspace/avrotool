
val readLatestGitTag = () => {
  import scala.sys.process.stringToProcess
  import scala.util._
  val TaggedCommitRegex = """^(\d+)\.(\d+)\.(\d+)$""".r
  val UntaggedCommitRegex = """^(\d+)\.(\d+)\.(\d+)\-\d+\-g([\w\d]+)""".r
  Try("git describe".!!.trim) match {
    case Success(desc @ TaggedCommitRegex(major, minor, patch)) => desc
    case Success(desc @ UntaggedCommitRegex(major, minor, patch, sha)) => desc
    case Failure(_) => "0.0.0"
  }
}

val writeVersionFile = Def.task {
  val versionFile = ((resourceManaged in Compile).value / "VERSION")
  streams.value.log.info(s"Generating version file $versionFile with contents ${version.value}")
  sbt.IO.write(versionFile, version.value)
  Seq(versionFile)
}

version in ThisBuild := readLatestGitTag()

resourceGenerators in Compile += writeVersionFile.taskValue

mappings in (Compile, packageBin) += {
  (resourceManaged in Compile).value / "VERSION" -> "out/example.txt"
}
