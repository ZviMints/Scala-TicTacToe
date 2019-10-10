package controllers

import javax.inject._
import play.api.mvc._
import models.Users
import play.api.libs.json._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Welcome"))
  }

  def logout = Action {
    Redirect(routes.HomeController.index()).withNewSession
  }

  def validateLogin = Action {
    request =>
      val values: Option[Map[String,Seq[String]]] = request.body.asFormUrlEncoded
      values.map { value =>
        val username = value("username").head
        val password = value("password").head
        if(Users.validate(username,password))
          Redirect(routes.GameController.index()).withSession("username" -> username) // With Routes
        else
          Ok(views.html.index(s"Cant Validate user " +
            s"username = '$username' and password = '$password'"))
      }.getOrElse(Ok(views.html.index("No body info, Try again.")))
  }

  def CreateUser = Action {
    request =>
      val values: Option[Map[String,Seq[String]]] = request.body.asFormUrlEncoded
      values.map { value =>
        val username = value("username").head
        val password = value("password").head
        if(Users.create(username,password)) {
          Ok(views.html.index("User Created Successfully."))
        }
        else
          Ok(views.html.index("User Already Created"))
      }.getOrElse(Ok(views.html.index("No body info, Try again.")))
  }
}
