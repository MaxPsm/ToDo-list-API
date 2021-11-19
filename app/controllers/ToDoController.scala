package controllers

import javax.inject._

import scala.concurrent.ExecutionContext

import models.{ToDo, TodoFormData}
import play.api.libs.json._
import play.api.mvc._
import services.TodoService

class ToDoController @Inject()(
  todoService: TodoService
)(implicit val ex: ExecutionContext) extends InjectedController {

  def getAll() = Action.async { implicit request: Request[AnyContent] =>
    todoService.listAllItems.map(items =>
      Ok(Json.toJson(items)))
    }

  def getById(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    todoService.getItem(id).map(item =>
      Ok(Json.toJson(item)))
  }

  def add() = Action.async(parse.json[TodoFormData]) { implicit request: Request[TodoFormData] =>
    val newTodoItem = ToDo(0, request.body.name, request.body.isComplete)
    todoService.addItem(newTodoItem).map(item =>
      Ok(Json.toJson(item)))
  }

  def update(id: Long) = Action.async(parse.json[TodoFormData]) { implicit request: Request[TodoFormData] =>
    val todoItem = ToDo(id, request.body.name, request.body.isComplete)
    todoService.updateItem(todoItem).map(item =>
      Ok(Json.toJson(item)))
  }

  def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteItem(id).map(item =>
      Ok(Json.toJson(item)))
  }

  def markComplete(id: Long, isComplete:Boolean) = Action.async { implicit request: Request[AnyContent] =>
    todoService.markIsCompleteItem(id,isComplete).map(item =>
      Ok(Json.toJson(item)))
  }

  def markToDos(isComplete: Boolean) = Action.async{implicit request: Request[AnyContent] =>
    todoService.markedIsCompleteAllItems(isComplete).map(item =>
      Ok(Json.toJson(item)))}

  def deleteCompleted = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteCompletedItems.map(item =>
      Ok(Json.toJson(item)))
  }
}