package info.acidflow.homer.modules.music.deezer.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
case class ListWrapper[T](data: List[T], total: Int, next: Option[String])
