package models

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.libs.json.Json
import slick.jdbc.MySQLProfile.api._

case class ToDo(id: Long, name: String, isComplete: Boolean)

trait ToDoJson {
  implicit val todoFormat = Json.format[ToDo]
}

object ToDo extends ToDoJson

case class TodoFormData(name: String, isComplete: Boolean)

object TodoFormData {
  implicit val todoFormReader = Json.reads[TodoFormData]
}

class TodoTableDef(tag: Tag) extends Table[ToDo](tag, "todo") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def isComplete = column[Boolean]("isComplete")

  override def * = (id, name, isComplete) <> ((ToDo.apply _).tupled, ToDo.unapply)
}