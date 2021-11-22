package daos

import models.ToDo
import slick.jdbc.MySQLProfile.api._

class ToDoTable(tag: Tag) extends Table[ToDo](tag, "todo") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def isComplete = column[Boolean]("is_complete")

  override def * = (id, name, isComplete) <> ((ToDo.apply _).tupled, ToDo.unapply)
}
