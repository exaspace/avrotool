package org.exaspace.avrotool

import org.scalatest.{FlatSpec, Matchers}


class VersionCommandSpec extends FlatSpec with Matchers {

  "VersionCommand" should "report the application version" in {
    val console = new TestingConsole
    new VersionCommand(console).reportVersion() shouldBe true
    console.stdout.trim should fullyMatch regex raw"^(\d+)\.(\d+)\.(\d+).*"
  }

  it should "read the application version from the system properties" in {
    val maybeCurrVersion = sys.props.get("prog.version")
    try {
      val console = new TestingConsole
      sys.props += "prog.version" -> "0.9.2323"
      new VersionCommand(console).reportVersion()
      console.stdout.trim shouldBe "0.9.2323"
    }
    finally {
      maybeCurrVersion.foreach(v => sys.props += "prog.version" -> v)
    }
  }

}
