package org.exaspace.avrotool

import org.scalatest.{FlatSpec, Matchers}

class CompatibilityLevelSpec extends FlatSpec with Matchers {

  "meetsLevel" should "work comparing to full" in {
    CompatibilityLevel.meetsLevel(Full, Full) shouldBe true
    CompatibilityLevel.meetsLevel(Forward, Full) shouldBe false
    CompatibilityLevel.meetsLevel(Backward, Full) shouldBe false
    CompatibilityLevel.meetsLevel(None, Full) shouldBe false
  }

  "meetsLevel" should "work comparing to forward" in {
    CompatibilityLevel.meetsLevel(Full, Forward) shouldBe true
    CompatibilityLevel.meetsLevel(Forward, Forward) shouldBe true
    CompatibilityLevel.meetsLevel(Backward, Forward) shouldBe false
    CompatibilityLevel.meetsLevel(None, Forward) shouldBe false
  }

  "meetsLevel" should "work comparing to backward" in {
    CompatibilityLevel.meetsLevel(Full, Backward) shouldBe true
    CompatibilityLevel.meetsLevel(Backward, Backward) shouldBe true
    CompatibilityLevel.meetsLevel(Forward, Backward) shouldBe false
    CompatibilityLevel.meetsLevel(None, Backward) shouldBe false
  }

  "meetsLevel" should "work for multiple levels" in {
    CompatibilityLevel.meetsLevel(Seq(Full, Backward, Full), Backward) shouldBe true
    CompatibilityLevel.meetsLevel(Seq(Full, Backward, Full), Forward) shouldBe false
  }

}

