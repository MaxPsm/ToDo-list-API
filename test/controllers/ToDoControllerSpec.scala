package controllers

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorSystem
import models.ToDo
import org.mockito.ArgumentMatchers.any
import org.mockito.MockitoSugar
import org.scalatest.MustMatchers
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
import services.TodoService

class ToDoControllerSpec extends PlaySpec
  with Results
  with GuiceOneAppPerSuite
  with MockitoSugar
  with MustMatchers {
  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  implicit val mat: ActorSystem = ActorSystem()

  "ToDos" should {
    val mockedService = mock[TodoService]
    val controller = new ToDoController(mockedService) {
      override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
    }

    "get should be valid" in {
      val req = FakeRequest(GET, "/todos")
      when( mockedService.listAllItems ).thenReturn(Future(Nil))

      val result: Future[Result] = controller.getAll().apply(req)
      status(result) mustBe OK
      contentAsJson(result) mustBe Json.toJson(Seq[ToDo]())
    }

    "update should be valid" in {
      val reqID = 1
      val sessionTask = ToDo(reqID, "eat", isComplete = false)
      val request = FakeRequest(PUT, s"/todos/update/$reqID")
        .withHeaders(CONTENT_TYPE -> JSON)
        .withBody(Json.toJson(sessionTask))

        when( mockedService.updateItem(any[ToDo]) ).thenReturn(Future(reqID))
        val method = call(controller.update(reqID), request)
        status(method) mustBe OK
    }

    "delete should be valid" in {
      val deleteId = 1
      val request = FakeRequest(GET, s"/todos/delete/$deleteId")

      val controller = new ToDoController(mockedService) {
        override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      }

      when( mockedService.deleteItem(deleteId) ).thenReturn(Future(deleteId))
      val method = controller.delete(deleteId).apply(request)
      status(method) mustBe OK
    }

    "create should be valid" in {
      val task_id = 1
      val newTask = ToDo(task_id, "eat", isComplete = false)
      val request = FakeRequest(POST, "/todos/add")
        .withHeaders(CONTENT_TYPE -> JSON)
        .withBody(Json.toJson(newTask))

      when( mockedService.addItem(any[ToDo]) ).thenReturn(Future("TodoItem successfully added"))

      val method = call(controller.add(), request)

      status(method) mustBe OK
    }

    "complete should be valid" in {
      val task_id = 1
      val completed = false
      val request = FakeRequest(PUT, s"/todos/mark/complete/$task_id")

      when( mockedService.markIsCompleteItem(task_id,completed) ).thenReturn(Future(task_id))

      val method = call(controller.markComplete(task_id,completed), request)

      status(method) mustBe OK
    }

    "complete all should be valid" in {
      val completed = false
      val request = FakeRequest(PUT, s"/todos/mark/all")

      when( mockedService.markedIsCompleteAllItems(completed)).thenReturn(Future(1))

      val method = call(controller.markToDos(completed),request)

      status(method) mustBe OK
    }

    "delete all completed should be valid" in {
      val request = FakeRequest(DELETE, "/todos/deleteCompleted")

      when( mockedService.deleteCompletedItems()).thenReturn(Future[1])

      val method = call(controller.deleteCompleted(),request)

      status(method) mustBe OK
    }
  }
}