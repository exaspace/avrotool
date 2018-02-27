package org.exaspace.avrotool


class DeleteSubjectCommand(console: ConsoleOutput) {

  def delete(subject: String, version: Option[String], schemaRegistryClient: RegistryClient): Boolean = {
    schemaRegistryClient.deleteSubject(subject, version) match {
      case Some(response) =>
        console.println(response)
        true
      case _ =>
        console.debug("Failed to delete schema")
        false
    }
  }

}

