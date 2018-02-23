package org.exaspace.avrotool

import org.scalatest.{FlatSpec, Matchers}


class VersionCommandSpec extends FlatSpec with Matchers with TempFileSupport {

  "VersionCommand" should "report the application version" in {
    val console = new TestingConsole
    new VersionCommand(console).reportVersion() shouldBe true
    console.stdout.trim should fullyMatch regex raw"^(\d+)\.(\d+)\.(\d+).*"
  }

}
