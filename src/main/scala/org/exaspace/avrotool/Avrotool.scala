package org.exaspace.avrotool

import java.nio.file.Paths


object Avrotool {

  def main(args: Array[String]): Unit = {

    val conf = new Args(args)

    val console = new ConsoleOutput {
      override def print(x: Any): Unit = System.out.print(x)
      override def println(x: Any): Unit = System.out.println(x)
      override def debug(x: Any): Unit = System.err.println(s"* $x")
      override def write(bytes: Array[Byte]): Unit = System.out.write(bytes)
    }

    val ok: Boolean = conf.action match {

      case Actions.CheckCompat =>
        new CheckCompatibilityCommand(console).check(
          conf.schemaFiles().map(Paths.get(_)),
          OutputFormat(conf.format()),
          conf.level.toOption.map(CompatibilityLevels(_)))

      case Actions.DecodeDatum =>
        new DecodeDatumCommand(console).decode(
          Paths.get(conf.datumFile()),
          registryClient(conf.schemaRegistryUrl()))

      case Actions.Register =>
        new RegisterSchemaCommand(console).register(
          Paths.get(conf.schemaFile()),
          conf.subject(),
          registryClient(conf.schemaRegistryUrl()))

      case Actions.UnwrapDatum =>
        new UnwrapCommand(console).unwrap(Paths.get(conf.datumFile()))

      case Actions.ValidateSchema =>
        new ValidateSchemaCommand(console).validate(
          Paths.get(conf.schemaFile()),
          OutputFormat(conf.format()))

      case _ =>
        println("No action specified")
        conf.printHelp()
        true
    }
    System.exit(if (ok) 0 else 1)
  }

  private def registryClient(url: String) = new RegistryHttpClient(url)

}
