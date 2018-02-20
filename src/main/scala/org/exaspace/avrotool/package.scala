package org.exaspace

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.file.Path

import org.apache.avro.Schema

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
    def debug(x: Any)
  }

  object ParseSchema {

    def fromFile(filePath: Path): Schema = {
      val url = filePath.toUri.toURL
      val is = url.openStream
      try {
        fromStream(is)
      } finally {
        is.close()
      }
    }

    def fromString(s: String): Schema = {
      val is = new ByteArrayInputStream(s.getBytes("UTF-8"))
      try {
        fromStream(is)
      } finally {
        is.close()
      }
    }

    private def fromStream(is: InputStream): Schema = new Schema.Parser().parse(is)

  }

}
