package org.exaspace.avrotool

import org.apache.avro.Schema
import org.scalatest.{FlatSpec, Matchers}


class RegisterSubjectCommandSpec extends FlatSpec with Matchers with TempFileSupport {

  val validSchema = """{  "type": "string",  "name": "foo"  }"""

  "RegisterSubjectCommand" should "return true and print ID when registering a subject" in {
    withTempFile(validSchema.toString) { schemaFile =>
      val console = new TestingConsole
      val testClient = new TestSchemaRegistry()
      new RegisterSubjectCommand(console).register(schemaFile, "subject", testClient) shouldBe true
      console.stdout shouldBe "123\n"
      testClient.subject shouldBe "subject"
      testClient.schema shouldBe ParseSchema.fromString(validSchema)
    }
  }

  class TestSchemaRegistry extends RegistryClient {

    var schema: Schema = null
    var subject: String = null

    override def register(subject: String, s: Schema): Option[Int] = {
      this.subject = subject
      this.schema = s
      Some(123)
    }

    override def fetchById(id: Int): Option[Schema] = ???

    override def deleteSubject(subject: String, version: Option[String]): Option[String] = ???
  }

}
