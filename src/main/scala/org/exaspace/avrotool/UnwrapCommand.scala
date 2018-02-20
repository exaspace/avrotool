package org.exaspace.avrotool

import java.nio.file.{Files, Path}


/**
  * Removes the initial Confluent 5 byte prefix
  */
class UnwrapCommand(console: ConsoleOutput) {

  def unwrap(datumFile: Path): Boolean = {
    try {
      val confluentBytes = Files.readAllBytes(datumFile)
      val confluentBinary = ConfluentBinary(confluentBytes)
      console.write(confluentBinary.avroBytes)
      true
    }
    catch {
      case _: Exception => false
    }
  }

}
