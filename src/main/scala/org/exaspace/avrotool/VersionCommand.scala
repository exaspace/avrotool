package org.exaspace.avrotool


class VersionCommand(console: ConsoleOutput) {

  def reportVersion(): Boolean = {
    val maybeVersion = tryToGetVersionFromSysProp()
    console.println(maybeVersion.getOrElse("0.0.0"))
    true
  }

  def tryToGetVersionFromSysProp(): Option[String] = sys.props.get("prog.version")

}
