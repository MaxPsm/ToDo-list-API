package daos

//import java.util.logging.Logger

import com.google.inject.Inject
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{ExecutionContext, Future}

//import org.slf4j.{Logger, LoggerFactory}
import play.api
import play.api.{Logger, Logging}

import io.sentry.Sentry


class ToDoDAO @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                       )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with Logging {

  var todoList = TableQuery[TodoTableDef]


  def add(todoItem: ToDo): Future[String] = {
    //LoggerFactory.getLogger("error").error("TodoItem successfully added")
    val addLogger: play.api.Logger = Logger("access")
    Sentry.init("https://3e0cf5f4808543c78f7b983210b426d1:fb9fa7a5a6344f3c95203f9d7e274bb0@sentry.kinoplan.tk/60")
    //Sentry.captureException(new Exception("TESYT"))
    try throw new Exception("This is a test.")
    catch {
      case e: Exception =>
        Sentry.captureException(e)
    }
    db
      .run(todoList += todoItem)
      .map{res =>
        addLogger.error("add message", new Exception("TESYT"))
        //logger.error("TodoItem successfully added")
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

  def markCompleteAll(isComplete: Boolean) = {
    db.run(todoList.map(x => x.isComplete).update(isComplete))
  }

  def deleteCompleted = {
    db.run(todoList.filter(_.isComplete === true).delete)
  }

}
