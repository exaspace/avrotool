package org.exaspace.avrotool

import java.nio.file.Path

import org.exaspace.avrotool.CompatibilityLevels.CompatibilityLevel
import spray.json.DefaultJsonProtocol._
import spray.json._


class CheckCompatibilityCommand(console: ConsoleOutput) {

  def check(schemaFiles: List[Path], format: OutputFormat, desiredLevel: Option[CompatibilityLevel]): Boolean = {
    val schemas = schemaFiles.map(ParseSchema.fromFile)
    val levels = CompatibilityChecker.findCompatibilityLevels(schemas)
    format match {
      case JsonOutput => reportJson(schemaFiles, levels)
      case PlainOutput => reportPlainText(levels)
    }
    desiredLevel.map(CompatibilityLevels.meetsLevel(levels, _)).getOrElse(true)
  }

  private def reportJson(files: Seq[Path], levels: Seq[CompatibilityLevel]): Unit = {
    val names = files.drop(1).map(_.getFileName.toString)
    val m: Map[String, String] = names.zip(levels.map(_.toString)).toMap
    console.println(m.toJson)
  }

  private def reportPlainText(levels: Seq[CompatibilityLevel]): Unit = {
    levels.mkString(" ").foreach(console.print)
    console.println("")
  }

}