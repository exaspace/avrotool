package org.exaspace.avrotool

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.file.{Path, Paths}

import org.apache.avro._


object Avrotool {

  def main(args: Array[String]): Unit = {

    val conf = new Args(args)

    val console = new ConsoleOutput {
      override def print(x: Any): Unit = System.out.print(x)
      override def println(x: Any): Unit = System.out.println(x)
    }

    val ok: Boolean = conf.action match {

      case Actions.CheckCompat =>
        new CheckCompatibilityCommand(console).check(
          conf.schemaFiles().map(Paths.get(_)),
          OutputFormat(conf.format()),
          conf.level.toOption.map(CompatibilityLevel(_)))

      case Actions.DecodeDatum =>
        new DecodeDatumCommand(console).decode(
          Paths.get(conf.datumFile()),
          registry(conf.schemaRegistryUrl()))

      case Actions.Register =>
        new RegisterSchemaCommand(console).register(
          Paths.get(conf.schemaFile()),
          conf.subject(),
          registry(conf.schemaRegistryUrl()))

      case _ =>
        println("No action specified")
        conf.printHelp()
        true
    }
    System.exit(if (ok) 0 else 1)
  }

  private def registry(url: String) = new RegistryHttpClient(url)

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

