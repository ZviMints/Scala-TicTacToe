package models
import scala.collection.mutable
import models.Board
object Users {

  private var users = mutable.Map[String,String]( "zvi" -> "pass" )
  private var game = mutable.Map[String,Board]()

  def validate(username: String, password: String): Boolean = {
    users.get(username).map( _ == password).getOrElse(false)
  }

  def create(username: String, password: String): Boolean = {
    if (users.contains(username))
      false
    else
      users(username) = password
      true
  }

  def getState(username: String):Board = game.get(username).getOrElse(Board())
  def setState(username: String, board: Board) = game(username) = board

}

