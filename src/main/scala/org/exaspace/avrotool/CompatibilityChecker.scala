package org.exaspace.avrotool

import org.apache.avro.{Schema, SchemaValidationException, SchemaValidator, SchemaValidatorBuilder}

import scala.collection.JavaConverters._


object CompatibilityChecker {

  val fullValidator = new SchemaValidatorBuilder().mutualReadStrategy.validateLatest
  val backwardValidator = new SchemaValidatorBuilder().canReadStrategy.validateLatest
  val forwardValidator = new SchemaValidatorBuilder().canBeReadStrategy.validateLatest

  def findCompatibilityLevels(schemas: Seq[Schema]): Seq[CompatibilityLevel] = {
    val mainSchema = schemas(0)
    schemas.drop(1).map { targetSchema =>
      findCompatibilityLevel(mainSchema, targetSchema)
    }
  }

  def findCompatibilityLevel(s1: Schema, s2: Schema): CompatibilityLevel = {
    if (runValidation(fullValidator, s1, s2)) Full
    else if (runValidation(backwardValidator, s1, s2)) Backward
    else if (runValidation(forwardValidator, s1, s2)) Forward
    else None
  }

  private def runValidation(v: SchemaValidator, s1: Schema, s2: Schema): Boolean = {
    try {
      v.validate(s1, Seq(s2).asJava)
      true
    } catch {
      case e: SchemaValidationException =>
        false
    }
  }

}