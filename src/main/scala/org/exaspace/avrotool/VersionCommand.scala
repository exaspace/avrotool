package org.exaspace.avrotool

import scala.io.Source


class VersionCommand(console: ConsoleOutput) {

  def reportVersion(): Boolean = {
    val maybeVersion = tryToGetVersionFromVersionFile()
    console.println(maybeVersion.getOrElse("0.0.0"))
    true
  }

  def tryToGetVersionFromVersionFile(): Option[String] =
    Option(getClass.getResourceAsStream("/VERSION")).map(Source.fromInputStream(_).getLines().next())

}
