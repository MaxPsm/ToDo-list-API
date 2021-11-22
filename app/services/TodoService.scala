package services

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

import daos.ToDoDAO
import models.ToDo

@Singleton
class TodoService @Inject()(itemsDAO: ToDoDAO) {

  def listAllItems: Future[Seq[ToDo]] = itemsDAO.listAll

  def getItem(id: Long): Future[Option[ToDo]] = itemsDAO.get(id)

  def addItem(item: ToDo): Future[String] = itemsDAO.add(item)

  def updateItem(item: ToDo): Future[Int] = itemsDAO.update(item)

  def markIsCompleteItem(id: Long, isComplete: Boolean): Future[Int] = itemsDAO.markComplete(id, isComplete)

  def markedIsCompleteAllItems(isComplete: Boolean): Future[Int] = itemsDAO.markCompleteAll(isComplete)

  def deleteItem(id: Long): Future[Int] = itemsDAO.delete(id)

  def deleteCompletedItems(): Future[Int] = itemsDAO.deleteCompleted()
}
