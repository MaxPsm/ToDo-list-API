package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json._
import models.{ToDo, TodoFormData}
import services.TodoService
import scala.concurrent.ExecutionContext.Implicits.global

import ch.qos.logback.classic.Logger



@Singleton
class ToDoController @Inject()(
  cc: ControllerComponents,
  todoService: TodoService
) extends AbstractController(cc) {

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
    todoService.addItem(newTodoItem).map(_ => Redirect(routes.ToDoController.getAll))
  }

  def update(id: Long) = Action.async(parse.json[TodoFormData]) { implicit request: Request[TodoFormData] =>
    val todoItem = ToDo(id, request.body.name, request.body.isComplete)
    todoService.updateItem(todoItem).map(_ => Redirect(routes.ToDoController.getAll))
  }

  def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteItem(id).map( _ => Redirect(routes.ToDoController.getAll))
  }

  def markComplete(id: Long, isComplete:Boolean) = Action.async { implicit request: Request[AnyContent] =>
    todoService.markIsCompleteItem(id,isComplete).map(_ => Redirect(routes.ToDoController.getAll))
  }

  def markToDos(isComplete: Boolean) = Action.async{implicit request: Request[AnyContent] =>
    todoService.markedIsCompleteAllItems(isComplete).map(_ => Redirect(routes.ToDoController.getAll))}

  def deleteCompleted = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteCompletedItems.map(_ => Redirect(routes.ToDoController.getAll))
  }
}