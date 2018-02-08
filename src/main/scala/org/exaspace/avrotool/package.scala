package org.exaspace

package object avrotool {

  sealed trait OutputFormat

  case object PlainOutput extends OutputFormat

  case object JsonOutput extends OutputFormat

  object OutputFormat {
    def apply(s: String) = s match {
      case "json" => JsonOutput
      case _ => PlainOutput
    }
  }

  trait ConsoleOutput {
    def print(x: Any)
    def println(x: Any)
  }

}
