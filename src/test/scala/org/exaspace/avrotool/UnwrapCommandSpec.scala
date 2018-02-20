package org.exaspace.avrotool

import org.scalatest.{FlatSpec, Matchers}


class UnwrapCommandSpec extends FlatSpec with Matchers with TempFiles {

  "UnwrapCommand" should "remove initial Confluent bytes if bytes appear to be valid" in {
    withTempFile(Array[Byte](0, 1, 2, 3, 4, 5)) { f =>
      val console = new TestingConsole
      new UnwrapCommand(console).unwrap(f) shouldBe true
      console.bytesWritten shouldBe Array[Byte](5)
    }
  }

  it should "fail if magic byte invalid" in {
    withTempFile(Array[Byte](111, 1, 2, 3, 4, 5)) { f =>
      val console = new TestingConsole
      new UnwrapCommand(console).unwrap(f) shouldBe false
    }
  }

}
