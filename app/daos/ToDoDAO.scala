package daos

import scala.concurrent.{ExecutionContext, Future}

import com.google.inject.Inject
import models._
import play.api.Logging
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._


class ToDoDAO @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                       )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with Logging {

  var todoList = TableQuery[TodoTableDef]


  def add(todoItem: ToDo): Future[String] = {
    db
      .run(todoList += todoItem)
      .map{res =>
        "TodoItem successfully added"
      }
      .recover {
        case ex: Exception => {
          printf(ex.getMessage)
          ex.getMessage
        }
      }
  }

  def delete(id: Long): Future[Int] = {
    db.run(todoList.filter(_.id === id).delete)
  }

  def update(todoItem: ToDo): Future[Int] = {
    db
      .run(todoList.filter(_.id === todoItem.id)
        .map(x => (x.name, x.isComplete))
        .update(todoItem.name, todoItem.isComplete)
      )
  }

  def get(id: Long): Future[Option[ToDo]] = {
    db.run(todoList.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[ToDo]] = {
    db.run(todoList.result)
  }

  def markComplete(id: Long, isComplete: Boolean): Future[Int] = {
    db.run(todoList.filter(_.id === id).map(x => x.isComplete).update(isComplete))
  }

  def markCompleteAll(isComplete: Boolean): Future[Int] = {
    db.run(todoList.map(x => x.isComplete).update(isComplete))
  }

  def deleteCompleted(): Future[Int] = {
    db.run(todoList.filter(_.isComplete === true).delete)
  }

}
