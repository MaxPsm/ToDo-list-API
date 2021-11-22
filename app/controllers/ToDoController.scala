package controllers

import javax.inject._

import scala.concurrent.ExecutionContext

import models.{ToDo, ToDoFormData}
import play.api.libs.json._
import play.api.mvc._
import services.TodoService

class ToDoController @Inject()(
  todoService: TodoService
)(implicit val ex: ExecutionContext) extends InjectedController {

  def getAll: Action[AnyContent] = Action.async {
    todoService.listAllItems.map(items =>
      Ok(Json.toJson(items))
    ).recover {
      _ => ServiceUnavailable
    }
  }

  def getById(id: Long): Action[AnyContent] = Action.async {
    todoService.getItem(id).map(item =>
      Ok(Json.toJson(item))
    )
  }

  def add(): Action[ToDoFormData] = Action.async(parse.json[ToDoFormData]) { request =>
    import request.{body => formData}

    val newTodoItem = ToDo(0, formData.name, formData.isComplete)

    todoService.addItem(newTodoItem).map(item =>
      Ok(Json.toJson(item))
    )
  }

  def update(id: Long): Action[ToDoFormData] = Action.async(parse.json[ToDoFormData]) { request =>
    val todoItem = ToDo(id, request.body.name, request.body.isComplete)

    todoService.updateItem(todoItem).map(item =>
      Ok(Json.toJson(item))
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteItem(id).map(item =>
      Ok(Json.toJson(item)))
  }

  def markComplete(id: Long, isComplete: Boolean): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    todoService.markIsCompleteItem(id, isComplete).map(item =>
      Ok(Json.toJson(item)))
  }

  def markToDos(isComplete: Boolean): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    todoService.markedIsCompleteAllItems(isComplete).map(item =>
      Ok(Json.toJson(item)))
  }

  def deleteCompleted(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteCompletedItems().map(item =>
      Ok(Json.toJson(item)))
  }
}