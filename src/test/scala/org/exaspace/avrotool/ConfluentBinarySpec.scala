package org.exaspace.avrotool

import org.scalatest.{FlatSpec, Matchers}


class ConfluentBinarySpec extends FlatSpec with Matchers {

  "ConfluentBinary" should "render" in {
    val cb = ConfluentBinary(0, 123, Array[Byte](10, 12, 33))
    cb.asBytes shouldBe Array[Byte](0, 0, 0, 0, 123, 10, 12, 33)
  }

  "ConfluentBinary" should "parse bytes" in {
    val cb = ConfluentBinary(Array[Byte](0, 0, 0, 0, 123, 10, 12, 33))
    cb.magic shouldBe 0
    cb.schemaId shouldBe 123
    cb.avroBytes shouldBe Array[Byte](10, 12, 33)
  }

}
