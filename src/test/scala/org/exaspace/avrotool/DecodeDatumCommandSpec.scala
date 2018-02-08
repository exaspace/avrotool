package org.exaspace.avrotool

import java.io.{ByteArrayOutputStream, File}
import java.nio.ByteBuffer
import java.nio.file.{Files, Paths}

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericDatumWriter}
import org.apache.avro.io.EncoderFactory
import org.scalatest.{FlatSpec, Matchers}


class DecodeDatumCommandSpec extends FlatSpec with Matchers {

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
    val testingRegistry = new TestingRegistry(userSchema)
    val datumFile = Paths.get(File.createTempFile("datum", ".tmp").getAbsolutePath)
    val record = new GenericData.Record(userSchema)
    record.put("name", "foo")
    val avroBytes = toAvro(record)
    Files.write(datumFile, toConfluentAvroBinary(123, avroBytes))
    new DecodeDatumCommand(testingConsole).decode(datumFile, testingRegistry) shouldBe true
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

}

class TestingConsole extends ConsoleOutput {

  val s = new StringBuilder

  override def print(x: Any): Unit = s.append(x)

  override def println(x: Any): Unit = s.append(x).append("\n")
}


class TestingRegistry(s: Schema) extends RegistryClient {

  override def register(subject: String, s: Schema): Option[Int] = ???

  override def fetchById(id: Int): Option[Schema] = Some(s)
}
