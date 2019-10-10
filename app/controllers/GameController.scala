package controllers

import javax.inject._
import play.api.mvc._
import models.Users
import play.api.libs.json._
import models._

@Singleton
class GameController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action { request =>
    val usernameOption: Option[String] = request.session.get("username")
    usernameOption.map { username =>
      val game : Board = Users.getState(username)
      Ok(views.html.game(game))
    }.getOrElse(Ok(views.html.index("No body info, Try again.")))
  }

  def playMade(i: Int, j: Int) = Action {
    request =>
      val usernameOption: Option[String] = request.session.get("username")
      usernameOption.map { username =>
        val game: Board = Users.getState(username)
        val newGame: Board = game.makeStep(i,j)
        val state = Brain.calculate(newGame)
        state match {
          case Winner(p) => Ok(views.html.cong(Player.opposite(newGame.playerTurn).toString,newGame))
          case Draw => Ok(views.html.draw(newGame))
          case inProgress =>
            Users.setState(username,newGame)
            Ok(views.html.game(newGame))
        }
      }.getOrElse(Ok(views.html.index("No body info, Try again.")))
  }

  def newGame = Action { request =>
    val usernameOption: Option[String] = request.session.get("username")
    usernameOption.map { username =>
      Users.setState(username, Board())
      Redirect(routes.GameController.index()).withSession("username" -> username) // With Routes
    }.getOrElse(Ok(views.html.index("No body info, Try again.")))
  }
}
