package org.exaspace.avrotool

import org.exaspace.avrotool.CompatibilityLevels._
import org.scalatest.{FlatSpec, Matchers}


class CompatibilityCheckerSpec extends FlatSpec with Matchers {

  val s1 = ParseSchema.fromString(
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

  val s2 = ParseSchema.fromString(
    """{
      |  "type": "record",
      |  "namespace": "example",
      |  "name": "User",
      |  "fields": [
      |    {
      |      "name": "name",
      |      "type": "string"
      |    },
      |    {
      |      "name": "number",
      |      "type": "int",
      |      "default": 0
      |    }
      |  ]
      |}""".stripMargin)

  val s3 = ParseSchema.fromString(
    """{
      |  "type": "record",
      |  "namespace": "example",
      |  "name": "User",
      |  "fields": [
      |    {
      |      "name": "name",
      |      "type": "string"
      |    },
      |    {
      |      "name": "number",
      |      "type": "int"
      |    },
      |    {
      |      "name": "colour",
      |      "type": "string",
      |      "default": "blue"
      |    }
      |  ]
      |}""".stripMargin)

  val s4 = ParseSchema.fromString(
    """{
      |  "type": "record",
      |  "namespace": "example",
      |  "name": "User",
      |  "fields": [
      |    {
      |      "name": "number",
      |      "type": "int",
      |      "default": 0
      |    },
      |    {
      |      "name": "colour",
      |      "type": "string"
      |    }
      |  ]
      |}""".stripMargin)

  "findCompatibilityLevel" should "report pairwise compatibility" in {
    CompatibilityChecker.findCompatibilityLevel(s4, s3) shouldBe Backward
    CompatibilityChecker.findCompatibilityLevel(s4, s2) shouldBe None
    CompatibilityChecker.findCompatibilityLevel(s4, s1) shouldBe Forward

    CompatibilityChecker.findCompatibilityLevel(s3, s2) shouldBe Full
    CompatibilityChecker.findCompatibilityLevel(s3, s1) shouldBe Forward

    CompatibilityChecker.findCompatibilityLevel(s2, s1) shouldBe Full
  }

}

