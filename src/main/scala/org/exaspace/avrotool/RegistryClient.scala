package org.exaspace.avrotool

import java.net.URI

import org.apache.avro.Schema
import spray.json.{DefaultJsonProtocol, JsString, _}

trait RegistryClient {
  def register(subject: String, s: Schema): Option[Int]
  def fetchById(id: Int): Option[Schema]
  def deleteSubject(subject: String, version: Option[String] = None): Option[String]
}

case class SchemaData(schema: String)

case class SchemaRegisterResponse(id: Int)

object RegistryProtocol extends DefaultJsonProtocol {
  implicit val f1 = jsonFormat1(SchemaData)
  implicit val f2 = jsonFormat1(SchemaRegisterResponse)
}

class RegistryHttpClient(schemaRegistryUrl: String) extends RegistryClient {

  import RegistryProtocol._
  import com.softwaremill.sttp._

  implicit val backend = HttpURLConnectionBackend()

  override def register(subject: String, s: Schema): Option[Int] = {
    val uri = Uri(new URI(schemaRegistryUrl)).path(s"/subjects/$subject/versions")
    val obj = SchemaData(s.toString).toJson.toString
    sttp.contentType("application/json").body(obj).post(uri).send().body match {
      case Right(responseBody) => Some(responseBody.parseJson.convertTo[SchemaRegisterResponse].id)
      case Left(_) => scala.None
    }
  }

  override def fetchById(id: Int): Option[Schema] = {
    val uri = Uri(new URI(schemaRegistryUrl)).path(s"/schemas/ids/$id")
    sttp.get(uri).send().body match {
      case Right(body) =>
        val js = body.parseJson.asJsObject
        val schemaStr = js.getFields("schema")(0).asInstanceOf[JsString].value
        Some(ParseSchema.fromString(schemaStr))
      case Left(_) => scala.None
    }
  }

  override def deleteSubject(subject: String, version: Option[String]): Option[String] = {
    val path = version.fold(s"/subjects/$subject")(_ => s"/subjects/$subject/versions/${version.get}")
    val uri = Uri(new URI(schemaRegistryUrl)).path(path)
    sttp.contentType("application/json").delete(uri).send().body match {
      case Right(responseBody) => Some(responseBody.parseJson.toString)
      case Left(_) => scala.None
    }
  }

}
