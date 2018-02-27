package org.exaspace.avrotool

import java.nio.file.Path


class RegisterSubjectCommand(console: ConsoleOutput) {

  def register(schemaFile: Path, subject: String, schemaRegistryClient: RegistryClient): Boolean = {
    schemaRegistryClient.register(subject, ParseSchema.fromFile(schemaFile)) match {
      case Some(id) =>
        console.println(id)
        true
      case _ =>
        false
    }
  }

}

