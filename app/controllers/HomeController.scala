package controllers

import javax.inject._
import play.api.mvc._
import models.Users
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(cc: ControllerComponents, ws: WSClient) extends AbstractController(cc) {

  def sendAPICall = Action.async { implicit request =>
    var (username, password) = ("zvi", 123)
    val ans = ws.url("http://localhost:9000/reciveAPICall").post(
      Json.obj("msg" -> "very important message")
    )
    ans map { response =>
      val statusText: String = response.body
      Ok(s"Got a response: \n $statusText")
    }
  }

  def reciveAPICall = Action {
    request =>
      request.body.asJson.map { json =>
        val msg = (json \ "msg").as[String]
        Ok(msg)
      }.getOrElse(Ok("Expecting application/json request body"))
  }

  def index = Action {
    Ok(views.html.index("Welcome"))
  }

  def logout = Action {
    Redirect(routes.HomeController.index()).withNewSession
  }

  def validateLogin = Action {
    request =>
      val values: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
      values.map { value =>
        val username = value("username").head
        val password = value("password").head
        if (Users.validate(username, password))
          Redirect(routes.GameController.index()).withSession("username" -> username) // With Routes
        else
          Ok(views.html.index(s"Cant Validate user " +
            s"username = '$username' and password = '$password'"))
      }.getOrElse(Ok(views.html.index("No body info, Try again.")))
  }

  def CreateUser = Action {
    request =>
      val values: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
      values.map { value =>
        val username = value("username").head
        val password = value("password").head
        if (Users.create(username, password)) {
          Ok(views.html.index("User Created Successfully."))
        }
        else
          Ok(views.html.index("User Already Created"))
      }.getOrElse(Ok(views.html.index("No body info, Try again.")))
  }

  def scores = Action {
    Ok(views.html.scores())
  }
}
