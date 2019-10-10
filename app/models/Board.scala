package models

import scala.util.{Success, Try}

sealed trait Player
case object X extends Player { override def toString: String = "X" }
case object O extends Player { override def toString: String = "O" }
case object None extends Player { override def toString: String = "Empty" }
object Player {
  def opposite(p: Player): Player = p match {
    case X => O
    case O => X
  }
}
case class Board(list: List[Player] = List.fill(9)(None), playerTurn: Player = X, steps:Int = 0) {

  def makeStep(i: Int, j: Int): Board = {
    val idx = i * 3  + j
    if(idx < 0 || idx > 8) this
    else {
      val (first,second) = list.splitAt(idx)
      second match {
        case None :: tail => Board(first ::: playerTurn :: tail,Player.opposite(playerTurn),steps+1)
        case _ => this // Invalid play
      }
    }
  }
  def getTurn :String = playerTurn.toString()
  def getStringByIndex(i: Int, j: Int): String = {
    val player : Try[Player] = Try(list(i * 3 + j))
    player match {
      case Success(value) => value.toString
      case exception: Exception => None.toString
    }
  }
}