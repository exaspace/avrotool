package org.exaspace.avrotool

import java.nio.file.Path


class RegisterSchemaCommand(console: ConsoleOutput) {

  def register(schemaFile: Path, subject: String, schemaRegistry: RegistryClient): Boolean = {
    schemaRegistry.register(subject, ParseSchema.fromFile(schemaFile)) match {
      case Some(id) =>
        console.println(id)
        true
      case _ =>
        false
    }
  }

}

