package controllers

import javax.inject._
import play.api.mvc._
import models.Users
import play.api.libs.json._
import models._
import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.duration._


@Singleton
class GameController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action { request =>
    val usernameOption: Option[String] = request.session.get("username")
    usernameOption.map { username =>
      val game : Board = Users.getState(username)
      val state = Brain.calculate(game)
      if(state == Winner(X) || state == Winner(O))
        Ok(views.html.cong(Player.opposite(game.playerTurn).toString,game))
      else
      Ok(views.html.game(game))
    }.getOrElse(Ok(views.html.index("No body info, Try again.")))
  }

  def playMade(i: Int, j: Int) = Action {
    request =>
      val usernameOption: Option[String] = request.session.get("username")
      usernameOption.map { username =>
        val game: Board = Users.getState(username)
        /** This is Safety Check for URL */
        val SafetyCheck = Brain.calculate(game)
        val continue = SafetyCheck match {
          case Winner(p) => false
          case Draw => false
          case _ => true
        }
        /** ---------------------------- */
        if(continue) {
          val newGame: Board = game.makeStep(i, j)
          val state = Brain.calculate(newGame)
          Users.setState(username, newGame)
          state match {
            case Winner(p) => Ok(views.html.cong(Player.opposite(newGame.playerTurn).toString, newGame))
            case Draw => Ok(views.html.draw(newGame))
            case inProgress => Ok(views.html.game(newGame))
          }
        }
        else
          BadRequest("Its Already Draw or Winning State!")
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
