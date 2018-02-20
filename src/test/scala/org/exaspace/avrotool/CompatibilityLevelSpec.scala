package org.exaspace.avrotool

import org.scalatest.{FlatSpec, Matchers}
import org.exaspace.avrotool.CompatibilityLevels._


class CompatibilityLevelSpec extends FlatSpec with Matchers {

  "meetsLevel" should "work comparing to full" in {
    meetsLevel(Full, Full) shouldBe true
    meetsLevel(Forward, Full) shouldBe false
    meetsLevel(Backward, Full) shouldBe false
    meetsLevel(None, Full) shouldBe false
  }

  "meetsLevel" should "work comparing to forward" in {
    meetsLevel(Full, Forward) shouldBe true
    meetsLevel(Forward, Forward) shouldBe true
    meetsLevel(Backward, Forward) shouldBe false
    meetsLevel(None, Forward) shouldBe false
  }

  "meetsLevel" should "work comparing to backward" in {
    meetsLevel(Full, Backward) shouldBe true
    meetsLevel(Backward, Backward) shouldBe true
    meetsLevel(Forward, Backward) shouldBe false
    meetsLevel(None, Backward) shouldBe false
  }

  "meetsLevel" should "work for multiple levels" in {
    meetsLevel(Seq(Full, Backward, Full), Backward) shouldBe true
    meetsLevel(Seq(Full, Backward, Full), Forward) shouldBe false
  }

}

