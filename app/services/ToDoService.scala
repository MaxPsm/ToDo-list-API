package services

import scala.concurrent.Future

import com.google.inject.Inject
import daos.ToDoDAO
import models.ToDo


class TodoService @Inject() (items: ToDoDAO) {

  def addItem(item: ToDo): Future[String] = {
    items.add(item)
  }

  def deleteItem(id: Long): Future[Int] = {
    items.delete(id)
  }

  def updateItem(item: ToDo): Future[Int] = {
    items.update(item)
  }

  def getItem(id: Long): Future[Option[ToDo]] = {
    items.get(id)
  }

  def listAllItems: Future[Seq[ToDo]] = {
    items.listAll
  }

  def markIsCompleteItem(id: Long, isComplete: Boolean): Future[Int] = {
    items.markComplete(id, isComplete)
  }

  def markedIsCompleteAllItems(isComplete: Boolean): Future[Int] = {
    items.markCompleteAll(isComplete)
  }

  def deleteCompletedItems = {
    items.deleteCompleted
  }
}