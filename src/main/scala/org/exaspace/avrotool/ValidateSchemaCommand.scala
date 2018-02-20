package org.exaspace.avrotool

import java.nio.file.Path

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.{Failure, Try}


/**
  * Simply parses a Schema file and exits 0 if valid, 1 if invalid
  */
class ValidateSchemaCommand(console: ConsoleOutput) {

  def validate(schemaFile: Path, format: OutputFormat): Boolean = {
    val ok = Try(ParseSchema.fromFile(schemaFile)) match {
      case Failure(e) =>
        console.debug(e)
        false
      case _ => true
    }
    format match {
      case PlainOutput => reportPlainText(ok)
      case JsonOutput => reportJson(ok, schemaFile)
    }
    ok
  }

  private def reportPlainText(ok: Boolean) = console.println(textStatus(ok))

  private def reportJson(ok: Boolean, path: Path) = {
    val m: Map[String, String] = Map(path.getFileName.toString -> textStatus(ok))
    console.println(m.toJson)
  }

  private def textStatus(ok: Boolean) = if (ok) "VALID" else "INVALID"

}
