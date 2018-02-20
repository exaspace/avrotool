package org.exaspace.avrotool

import java.io.ByteArrayInputStream
import java.nio.file.{Files, Path}

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.DecoderFactory


class DecodeDatumCommand(console: ConsoleOutput) {

  def decode(datumFileName: Path, schemaRegistryClient: RegistryClient): Boolean = {
    val datumBytes = Files.readAllBytes(datumFileName)

    val confluentBinary = ConfluentBinary(datumBytes)
    console.debug(s"schemaId: ${confluentBinary.schemaId}")

    val maybeSchema = schemaRegistryClient.fetchById(confluentBinary.schemaId)
    maybeSchema match {
      case Some(schema) =>
        val record = unmarshal(confluentBinary.avroBytes, schema, schema)
        console.println(record)
        true
      case scala.None =>
        false
    }
  }

  def decode(datumFileName: Path, writerSchema: Schema): Boolean = {
    val datumBytes = Files.readAllBytes(datumFileName)
    val record = unmarshal(ConfluentBinary(datumBytes).avroBytes, writerSchema, writerSchema)
    console.println(record)
    true
  }

  def decode(datumFileName: Path, writerSchema: Schema, readerSchema: Schema): Boolean = {
    val datumBytes = Files.readAllBytes(datumFileName)
    val record = unmarshal(ConfluentBinary(datumBytes).avroBytes, writerSchema, readerSchema)
    console.println(record)
    true
  }

  private def unmarshal(bytes: Array[Byte], writerSchema: Schema, readerSchema: Schema): GenericRecord = {
    val reader = new GenericDatumReader[GenericRecord](writerSchema, readerSchema)
    val decoderFactory = DecoderFactory.get()
    val inputStream = new ByteArrayInputStream(bytes)
    val decoder = decoderFactory.directBinaryDecoder(inputStream, null)
    reader.read(null, decoder)
  }

}
