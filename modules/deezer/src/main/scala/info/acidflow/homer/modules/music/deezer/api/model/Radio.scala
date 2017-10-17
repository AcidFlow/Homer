package info.acidflow.homer.modules.music.deezer.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
case class Radio(id: String, title: String)
