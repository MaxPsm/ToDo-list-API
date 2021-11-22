package models

import play.api.libs.json.Json.MacroOptions
import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration, OFormat}

case class ToDo(id: Long, name: String, isComplete: Boolean)

trait ToDoJson {
  implicit val config: JsonConfiguration.Aux[MacroOptions] = JsonConfiguration(SnakeCase)
  implicit val todoFormat: OFormat[ToDo] = Json.format[ToDo]
}

object ToDo extends ToDoJson