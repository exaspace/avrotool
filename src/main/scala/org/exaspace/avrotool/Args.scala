package org.exaspace.avrotool

import org.rogach.scallop.ScallopConf


object Actions {
  val CheckCompat = "checkcompat"
  val DecodeDatum = "decode-datum"
  val Register = "register"
  val ValidateSchema = "validate-schema"
}

class Args(arguments: Seq[String]) extends ScallopConf(arguments) {

  private val formatValidation = (s: String) => s == "json" || s == "plain"
  private val validateSchemaFilesArg = (files: List[String]) => files.length >= 2

  val checkcompat = opt[Boolean](
    name = Actions.CheckCompat,
    noshort = true,
    descr =
    """
      |check compatibility between schemas (compare first one to the other schemas in order)
    """.stripMargin)

  val decodeDatum = opt[Boolean](
    name = Actions.DecodeDatum,
    noshort = true,
    descr ="""decode a confluent encoded avro datum (requires --datum-file and --schema-registry-url)""")

  val register = opt[Boolean](
    name = Actions.Register,
    noshort = true,
    descr = "register a schema (requires --schema-files, --schema-registry-url and --subject)")

  val validateSchema = opt[Boolean](
    name = Actions.ValidateSchema,
    noshort = true,
    descr = "validate a schema (requires --schema-file)")



  val schemaFile = opt[String](
    name = "schema-file",
    noshort = true,
    descr =
      """
        |an Avro schema filename
      """.stripMargin)

  val schemaFiles = opt[List[String]](
    name = "schema-files",
    noshort = true,
    validate = validateSchemaFilesArg,
    descr =
      """
        |list of schema files
      """.stripMargin)

  val format = opt[String](
    name = "format",
    noshort = true,
    descr ="""output format: either "plain" or "json" """,
    default = Some("plain"),
    validate = formatValidation)

  val level = opt[String](
    name = "level",
    noshort = true,
    descr = "compatibility level")

  val schemaRegistryUrl = opt[String](
    name = "schema-registry-url",
    noshort = true)

  val datumFile = opt[String](
    name = "datum-file",
    noshort = true,
    descr = "a file containing a single datum encoded in Confluent Avro binary format")

  val subject = opt[String](
    name = "subject",
    noshort = true,
    descr = "schema registry subject")

  val verbose = opt[Boolean](
    name = "verbose",
    noshort = false,
    descr = "log verbose info")

  private val actions = Seq(checkcompat, decodeDatum, register, validateSchema)

  dependsOnAll(checkcompat, List(schemaFiles))
  dependsOnAll(decodeDatum, List(datumFile, schemaRegistryUrl))
  dependsOnAll(register, List(schemaFile, subject, schemaRegistryUrl))
  dependsOnAll(validateSchema, List(schemaFile))
  mutuallyExclusive(actions: _*)
  requireOne(actions: _*)
  verify()

  def action: String = actions.filter(_.isSupplied).head.name

}
