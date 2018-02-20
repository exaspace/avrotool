package org.exaspace.avrotool

import java.nio.file.{Files, Path}


/**
  * Apply the initial Confluent 5 byte prefix to a plain Avro binary datum and write the result to stdout.
  *
  * i.e. given some Avro bytes, write 0 (magic byte) + schemaId (4 bytes) + avroBytes
  */
class WrapCommand(console: ConsoleOutput) {

  def wrap(plainDatumFile: Path, schemaId: Int): Boolean = {
    try {
      if (schemaId < 0) sys.error("SchemaId must be a positive integer")
      val avroBytes = Files.readAllBytes(plainDatumFile)
      val confluentBinary = ConfluentBinary(0, schemaId, avroBytes)
      console.write(confluentBinary.asBytes)
      true
    }
    catch {
      case e: Exception =>
        console.debug(e.toString)
        false
    }
  }

}
