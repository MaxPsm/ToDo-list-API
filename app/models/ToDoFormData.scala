package models

import play.api.libs.json.Json.MacroOptions
import play.api.libs.json.{Json, JsonConfiguration, Reads}
import play.api.libs.json.JsonNaming.SnakeCase

case class ToDoFormData(name: String, isComplete: Boolean)

trait ToDoFormDataJson {
  implicit val config: JsonConfiguration.Aux[MacroOptions] = JsonConfiguration(SnakeCase)
  implicit val todoFormReader: Reads[ToDoFormData] = Json.reads[ToDoFormData]
}

object ToDoFormData extends ToDoFormDataJson
