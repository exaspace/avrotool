package org.exaspace.avrotool

import org.scalatest.{FlatSpec, Matchers}


class WrapCommandSpec extends FlatSpec with Matchers with TempFileSupport {

  "WrapCommand" should "add initial Confluent bytes" in {
    withTempFile(Array[Byte](1, 2)) { plainAvroFile =>
      val console = new TestingConsole
      new WrapCommand(console).wrap(plainAvroFile, 99) shouldBe true
      console.bytesWritten shouldBe Array[Byte](0, 0, 0, 0, 99, 1, 2)
    }
  }

  it should "fail if schema id is negative" in {
    withTempFile(Array[Byte](1)) { f =>
      val console = new TestingConsole
      new WrapCommand(console).wrap(f, -1) shouldBe false
    }
  }

}
