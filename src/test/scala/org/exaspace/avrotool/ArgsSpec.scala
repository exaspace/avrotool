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

  it should "accept --datum-file and --writer-schema-file" in {
    val conf = new Args(Seq("--decode-datum", "--datum-file", "foo.cavro", "--writer-schema-file", "schema.json"))
    conf.decodeDatum() shouldBe true
    conf.datumFile() shouldBe "foo.cavro"
    conf.writerSchemaFile() shouldBe "schema.json"
  }

  it should "fail if missing --datum-file" in {
    shouldFail(new Args(Seq("--decode-datum", "--schema-registry-url", "http://someurl")))
  }

  it should "fail if missing --schema-registry-url and --writer-schema-file" in {
    shouldFail(new Args(Seq("--decode-datum", "--datum-file", "foo.avro")))
  }

  it should "fail if only --reader-schema-file provided" in {
    shouldFail(new Args(Seq("--decode-datum", "--datum-file", "foo.avro", "--reader-schema-file", "schema.json")))
  }

  "--delete" should "accept --subject and --schema-registry-url" in {
    val conf = new Args(Seq("--delete", "--subject", "foobar", "--schema-registry-url", "http://someurl/"))
    conf.delete() shouldBe true
    conf.subject() shouldBe "foobar"
    conf.schemaRegistryUrl() shouldBe "http://someurl/"
  }

  it should "fail if missing --subject" in {
    shouldFail(new Args(Seq("--delete", "--schema-registry-url", "http://someurl")))
  }

  it should "accept --subject and --schema-registry-url and --subject-version" in {
    val conf = new Args(Seq("--delete", "--subject", "xx", "--subject-version", "22", "--schema-registry-url", "http://someurl/"))
    conf.delete() shouldBe true
    conf.subject() shouldBe "xx"
    conf.subjectVersion() shouldBe "22"
    conf.schemaRegistryUrl() shouldBe "http://someurl/"
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

  "--unwrap-datum" should "accept --datum-file" in {
    val conf = new Args(Seq("--unwrap-datum", "--datum-file", "foo.confluent_avro"))
    conf.unwrapDatum() shouldBe true
  }

  it should "fail if missing --datum-file" in {
    shouldFail(new Args(Seq("--unwrap-datum")))
  }

  "--validate-schema" should "accept --schema-file" in {
    val conf = new Args(Seq("--validate-schema", "--schema-file", "foo.avsc"))
    conf.validateSchema() shouldBe true
    conf.schemaFile() shouldBe "foo.avsc"
  }

  it should "fail if missing --schema-file" in {
    shouldFail(new Args(Seq("--validate-schema")))
  }

  "--wrap-datum" should "accept --datum-file and --schema-id" in {
    val conf = new Args(Seq("--wrap-datum", "--datum-file", "foo.avro", "--schema-id", "123"))
    conf.wrapDatum() shouldBe true
    conf.schemaId() shouldBe 123
  }

  it should "fail if missing --datum-file" in {
    shouldFail(new Args(Seq("--wrap-datum", "--schema-id", "123")))
  }

  it should "fail if missing --schema-id" in {
    shouldFail(new Args(Seq("--wrap-datum", "--datum-file", "1.avro")))
  }

  "Scallop version" should "be set from sys props" in {
    val conf = new Args(Seq("--decode-datum", "--datum-file", "foo", "--schema-registry-url", "http://someurl"))
    val maybeCurrVersion = sys.props.get("prog.version")
    val maybeCurrRevision = sys.props.get("prog.revision")
    try {
      sys.props += "prog.version" -> "0.9.2323"
      sys.props += "prog.revision" -> "abcdef"
      conf.versionFromSysProp shouldBe "0.9.2323 abcdef"
    }
    finally {
      maybeCurrVersion.foreach(v => sys.props += "prog.version" -> v)
      maybeCurrRevision.foreach(v => sys.props += "prog.revision" -> v)
    }
  }

  private def shouldFail(thunk: => Unit) = {
    throwError.withValue(true) {
      assertThrows[Exception] {
        thunk
      }
    }
  }

}
