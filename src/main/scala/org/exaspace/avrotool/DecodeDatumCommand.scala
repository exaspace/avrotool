package org.exaspace.avrotool

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.nio.file.{Files, Path, Paths}

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.DecoderFactory


class DecodeDatumCommand(console: ConsoleOutput) {

  def decode(fileName: Path, schemaRegistry: RegistryClient): Boolean = {
    val bytes = Files.readAllBytes(fileName)
    val byteBuffer = ByteBuffer.wrap(bytes)
    val magic = byteBuffer.get()
    val schemaId = byteBuffer.getInt()
    val length = byteBuffer.limit() - 5
    val remainingBytes = byteBuffer.array().slice(byteBuffer.position(), byteBuffer.limit())
    console.println(s"schemaId=$schemaId")

    val maybeSchema = schemaRegistry.fetchById(schemaId)
    maybeSchema.map { schema =>
      val record = unmarshal(schema, schema, remainingBytes)
      console.println(record)
      true
    }.getOrElse(false)
  }

  private def unmarshal(writerSchema: Schema, readerSchema: Schema, bytes: Array[Byte]): GenericRecord = {
    val reader = new GenericDatumReader[GenericRecord](writerSchema, readerSchema)
    val decoderFactory = DecoderFactory.get()
    val inputStream = new ByteArrayInputStream(bytes)
    val decoder = decoderFactory.directBinaryDecoder(inputStream, null)
    reader.read(null, decoder)
  }

}
