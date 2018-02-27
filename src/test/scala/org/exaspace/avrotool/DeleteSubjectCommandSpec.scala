package org.exaspace.avrotool

import org.apache.avro.Schema
import org.scalatest.{FlatSpec, Matchers}


class DeleteSubjectCommandSpec extends FlatSpec with Matchers with TempFileSupport {

  "DeleteSubjectCommand" should "return true and print response when deleting a subject" in {
    val console = new TestingConsole
    val testClient = new TestSchemaRegistry()
    new DeleteSubjectCommand(console).delete("subject", None, testClient) shouldBe true
    console.stdout shouldBe "some registry response\n"
    testClient.subject shouldBe "subject"
    testClient.version shouldBe None
  }

  class TestSchemaRegistry extends RegistryClient {

    var subject: String = null
    var version: Option[String] = null

    override def register(subject: String, s: Schema): Option[Int] = ???

    override def fetchById(id: Int): Option[Schema] = ???

    override def deleteSubject(subject: String, version: Option[String]): Option[String] = {
      this.subject = subject
      this.version = version
      Some("some registry response")
    }
  }

}
