package org.exaspace.avrotool

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericDatumWriter}
import org.apache.avro.io.EncoderFactory
import org.scalatest.{FlatSpec, Matchers}


class DecodeDatumCommandSpec extends FlatSpec with Matchers with TempFiles {

  val userSchema = ParseSchema.fromString(
    """{
      |  "type": "record",
      |  "namespace": "example",
      |  "name": "User",
      |  "fields": [
      |    {
      |      "name": "name",
      |      "type": "string",
      |      "default": ""
      |    }
      |  ]
      |}""".stripMargin)


  val testingConsole = new TestingConsole

  "DecodeDatumCommand" should "decode a datum file using schema from the registry" in {
    val testingRegistry = new TestSchemaRegistry(userSchema)
    val record = new GenericData.Record(userSchema)
    record.put("name", "foo")
    val avroBytes = toAvro(record)
    val confluentWrappedBytes = toConfluentAvroBinary(123, avroBytes)
    withTempFile(confluentWrappedBytes) { path =>
      new DecodeDatumCommand(testingConsole).decode(path, testingRegistry) shouldBe true
    }
  }

  def toConfluentAvroBinary(id: Int, b: Array[Byte]): Array[Byte] = {
    val bb = ByteBuffer.allocate(5 + b.length)
    bb.put(0.toByte)
    bb.putInt(id)
    bb.put(b)
    bb.array
  }

  def toAvro(rec: GenericData.Record): Array[Byte] = {
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
