package org.exaspace.avrotool

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.nio.file.{Files, Path}

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.DecoderFactory


class DecodeDatumCommand(console: ConsoleOutput) {

  def decode(datumFileName: Path, schemaRegistryClient: RegistryClient): Boolean = {
    val datumBytes = Files.readAllBytes(datumFileName)
    val byteBuffer = ByteBuffer.wrap(datumBytes)
    val magic = byteBuffer.get()
    assert(magic == 0x0, "Magic byte must be zero")
    val schemaId = byteBuffer.getInt()
    val length = byteBuffer.limit() - 5
    val avroBytes = byteBuffer.array().slice(byteBuffer.position(), byteBuffer.limit())
    console.debug(s"schemaId=$schemaId")

    val maybeSchema = schemaRegistryClient.fetchById(schemaId)
    maybeSchema match {
      case Some(schema) =>
        val record = unmarshal(avroBytes, schema, schema)
        console.println(record)
        true
      case scala.None =>
        false
    }
  }

  private def unmarshal(bytes: Array[Byte], writerSchema: Schema, readerSchema: Schema): GenericRecord = {
    val reader = new GenericDatumReader[GenericRecord](writerSchema, readerSchema)
    val decoderFactory = DecoderFactory.get()
    val inputStream = new ByteArrayInputStream(bytes)
    val decoder = decoderFactory.directBinaryDecoder(inputStream, null)
    reader.read(null, decoder)
  }

}
