package models

sealed trait State
case class Winner(p: Player) extends State
case object Draw extends State
case object inProgress extends State
