package org.exaspace.avrotool

import org.rogach.scallop.throwError
import org.scalatest.{FlatSpec, Matchers}


class ArgsSpec extends FlatSpec with Matchers {

  "action" should "be required" in {
    shouldFail(        new Args(Seq()))
  }

  "--checkcompat" should "require 2 or more schemas" in {
    shouldFail(new Args(Seq("--checkcompat", "--schema-files", "foo")))
  }

  it should "require --schema-files" in {
    shouldFail(new Args(Seq("--checkcompat")))
  }

  it should "support 2 schemas" in {
    val conf = new Args(Seq("--checkcompat", "--schema-files", "foo", "bar"))
    conf.schemaFiles() shouldBe Seq("foo", "bar")
  }

  it should "support 3 schemas" in {
    val conf = new Args(Seq("--checkcompat", "--schema-files", "foo", "bar", "baz"))
    conf.schemaFiles() shouldBe Seq("foo", "bar", "baz")
  }

  it should "use plain format by default" in {
    val conf = new Args(Seq("--checkcompat", "--schema-files", "foo", "bar"))
    conf.format() shouldBe "plain"
  }

  it should "use json format if specified" in {
    val conf = new Args(Seq("--checkcompat", "--schema-files", "foo", "bar", "--format", "json"))
    conf.format() shouldBe "json"
  }

  it should "reject invalid formats" in {
     shouldFail(new Args(Seq("--checkcompat", "--schema-files", "foo", "bar", "--format", "frobbler")))
  }

  "--decode-datum" should "accept --datum-file and --schema-registry-url" in {
    val conf = new Args(Seq("--decode-datum", "--datum-file", "foo.avro", "--schema-registry-url", "http://someurl"))
    conf.decodeDatum() shouldBe true
    conf.datumFile() shouldBe "foo.avro"
    conf.schemaRegistryUrl() shouldBe "http://someurl"
  }

  it should "fail if missing --datum-file" in {
    shouldFail(new Args(Seq("--decode-datum", "--schema-registry-url", "http://someurl")))
  }

  it should "fail if missing --schema-registry-url" in {
    shouldFail(new Args(Seq("--decode-datum", "--datum-file", "foo.avro")))
  }

  "--register" should "accept --schema-file and --schema-registry-url" in {
    val conf = new Args(Seq("--register", "--schema-file", "foo.avsc", "--subject", "foo", "--schema-registry-url", "http://someurl"))
    conf.register() shouldBe true
    conf.schemaFile() shouldBe "foo.avsc"
    conf.schemaRegistryUrl() shouldBe "http://someurl"
  }

  it should "fail if missing --subject" in {
    shouldFail(new Args(Seq("--register", "--schema-file", "foo.avsc", "--schema-registry-url", "http://someurl")))
  }

  it should "fail if missing --schema-file" in {
    shouldFail(new Args(Seq("--register", "--subject", "foo", "--schema-registry-url", "http://someurl")))
  }

  it should "fail if missing --schema-registry-url" in {
    shouldFail(new Args(Seq("--register", "--schema-file", "foo.avsc", "--subject", "foo")))
  }

  private def shouldFail(thunk: => Unit) = {
    throwError.withValue(true) {
      assertThrows[Exception] {
        thunk
      }
    }
  }

}
