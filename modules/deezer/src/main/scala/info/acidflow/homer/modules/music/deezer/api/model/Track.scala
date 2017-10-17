package info.acidflow.homer.modules.music.deezer.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
case class Track(id: String, title: String) {

  def deezerUri: String = {
    s"dzmedia:///track/$id"
  }
}
