package org.exaspace.avrotool

import org.scalatest.{FlatSpec, Matchers}
import spray.json._


class ValidateSchemaCommandSpec extends FlatSpec with Matchers with TempFiles {

  val validSchema = """{  "type": "string",  "name": "foo"  }"""
  val invalidSchema = """{  "type": "str",  "name": "foo"  }"""

  "ValidateSchemaCommand" should "return true if schema is a valid Avro schema" in {
    withTempFile(validSchema.toString) { schemaFile =>
      val console = new TestingConsole
      new ValidateSchemaCommand(console).validate(schemaFile, PlainOutput) shouldBe true
      console.stdout shouldBe "VALID\n"
    }
  }

  "ValidateSchemaCommand" should "output json when needed for VALID file" in {
    withTempFile(validSchema.toString) { schemaFile =>
      val console = new TestingConsole
      new ValidateSchemaCommand(console).validate(schemaFile, JsonOutput) shouldBe true
      console.stdout.parseJson shouldBe s""" { "${schemaFile.getFileName}": "VALID" } """.parseJson
    }
  }

  "ValidateSchemaCommand" should "return false if schema is an invalid Avro schema" in {
    withTempFile(invalidSchema) { schemaFile =>
      val console = new TestingConsole
      new ValidateSchemaCommand(console).validate(schemaFile, PlainOutput) shouldBe false
      console.stdout shouldBe "INVALID\n"
    }
  }

  "ValidateSchemaCommand" should "output json when needed for INVALID file" in {
    withTempFile(invalidSchema.toString) { schemaFile =>
      val console = new TestingConsole
      new ValidateSchemaCommand(console).validate(schemaFile, JsonOutput) shouldBe false
      console.stdout.parseJson shouldBe s""" { "${schemaFile.getFileName}": "INVALID" } """.parseJson
    }
  }

}
