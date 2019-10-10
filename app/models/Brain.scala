package models

object Brain {


  def calculate(game: Board): State = {
    val list : List[Player] = game.list
    if(diagonalWinner(list) || winnerColumn(list) || winnerRow(list))
      Winner(game.playerTurn)
    else if(noMoreSpace(list))
      Draw
    else
      inProgress
  }
  def winnerColumn(list: List[Player]): Boolean = list match {
    case List(X,_,_,
              X,_,_,
              X,_,_) => true
    case List(_,X,_,
              _,X,_,
              _,X,_) => true
    case List(_,_,X,
              _,_,X,
              _,_,X) => true
    case List(O,_,_,
              O,_,_,
              O,_,_) => true
    case List(_,O,_,
              _,O,_,
              _,O,_) => true
    case List(_,_,O,
              _,_,O,
              _,_,O) => true
    case _ => false
  }
  def winnerRow(list: List[Player]): Boolean = list match {
    case List(X,X,X,
              _,_,_,
              _,_,_) => true
    case List(_,_,_,
              X,X,X,
              _,_,_) => true
    case List(_,_,_,
              _,_,_,
              X,X,X) => true
    case List(O,O,O,
              _,_,_,
              _,_,_) => true
    case List(_,_,_,
              O,O,O,
              _,_,_) => true
    case List(_,_,_,
              _,_,_,
              O,O,O) => true
    case _ => false
  }


  def diagonalWinner(list: List[Player]): Boolean = list match {
    case List(X,_,_,
              _,X,_,
              _,_,X) => true
    case List(O,_,_,
              _,O,_,
              _,_,O) => true
    case List(_,_,X,
              _,X,_,
              X,_,_) => true
    case List(_,_,O,
              _,O,_,
              O,_,_) => true
    case _ => false
  }
  def noMoreSpace(list: List[Player]): Boolean = !list.contains(None)
}
