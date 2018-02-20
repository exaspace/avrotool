package org.exaspace.avrotool

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericDatumWriter}
import org.apache.avro.io.EncoderFactory
import org.scalatest.{FlatSpec, Matchers}
import spray.json._


class DecodeDatumCommandSpec extends FlatSpec with Matchers with TempFileSupport {

  val userSchema = ParseSchema.fromString(
    """{
        "type": "record",
        "namespace": "example",
        "name": "User",
        "fields": [
          {
            "name": "name",
            "type": "string",
            "default": ""
          }
        ]
      }""")

  val userSchema2 = ParseSchema.fromString(
    """{
        "type": "record",
        "namespace": "example",
        "name": "User",
        "fields": [
          {
            "name": "name",
            "type": "string",
            "default": ""
          },
          {
            "name": "age",
            "type": "int",
            "default": 99
          }
       ]
      }""")



  "DecodeDatumCommand" should "decode a datum file by fetching schema from the registry" in {
    val testingRegistry = new TestSchemaRegistry(userSchema)
    val record = new GenericData.Record(userSchema)
    record.put("name", "foo")
    val avroBytes = toAvroBinary(record)
    val confluentWrappedBytes = toConfluentAvroBinary(123, avroBytes)
    withTempFile(confluentWrappedBytes) { path =>
      val testingConsole = new TestingConsole
      new DecodeDatumCommand(testingConsole).decode(path, testingRegistry) shouldBe true
    }
  }

  "DecodeDatumCommand" should "decode a datum file using a specified writer schema file" in {
    val record = new GenericData.Record(userSchema)
    record.put("name", "foo")
    val avroBytes = toAvroBinary(record)
    val confluentBytes = toConfluentAvroBinary(123, avroBytes)
    withTempFile(confluentBytes) { datumFile =>
      val testingConsole = new TestingConsole
      new DecodeDatumCommand(testingConsole).decode(datumFile, userSchema) shouldBe true
      testingConsole.stdout.parseJson shouldBe """{"name":"foo"}""".parseJson
    }
  }

  "DecodeDatumCommand" should "decode a datum file using a specified reader & writer schema files" in {
    val record = new GenericData.Record(userSchema)
    record.put("name", "foo")
    val avroBytes = toAvroBinary(record)
    val confluentBytes = toConfluentAvroBinary(123, avroBytes)
    withTempFile(confluentBytes) { datumFile =>
      val testingConsole = new TestingConsole
      new DecodeDatumCommand(testingConsole).decode(datumFile, writerSchema = userSchema, readerSchema = userSchema2) shouldBe true
      testingConsole.stdout.parseJson shouldBe """{"name":"foo", "age": 99}""".parseJson
    }
  }

  def toConfluentAvroBinary(id: Int, b: Array[Byte]): Array[Byte] = {
    val bb = ByteBuffer.allocate(5 + b.length)
    bb.put(0.toByte)
    bb.putInt(id)
    bb.put(b)
    bb.array
  }

  def toAvroBinary(rec: GenericData.Record): Array[Byte] = {
    val gdw = new GenericDatumWriter[GenericData.Record](rec.getSchema)
    val out = new ByteArrayOutputStream
    val encoder = EncoderFactory.get().binaryEncoder(out, null)
    gdw.write(rec, encoder)
    encoder.flush()
    out.close()
    out.toByteArray()
  }

  class TestSchemaRegistry(s: Schema) extends RegistryClient {

    override def register(subject: String, s: Schema): Option[Int] = ???

    override def fetchById(id: Int): Option[Schema] = Some(s)
  }

}
