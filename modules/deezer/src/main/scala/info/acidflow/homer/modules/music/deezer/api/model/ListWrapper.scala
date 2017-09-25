package info.acidflow.homer.modules.music.deezer.api.model

case class ListWrapper[T](data : List[T], total : Int, next : Option[String])
