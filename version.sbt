
val versionFromGitDescribe = () => {
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
version in ThisBuild := versionFromGitDescribe()
