package info.acidflow.homer.modules.music.deezer.api

import info.acidflow.homer.modules.music.deezer.api.model.{Artist, ListWrapper, Radio, Track}
import retrofit2.Call
import retrofit2.http.{GET, Path, Query}


trait DeezerApiService {

  @GET("/user/me")
  def getMyUser(@Query("access_token") accessToken: String): Call[Artist]

  @GET("/radio/lists")
  def getRadios(@Query("access_token") accessToken: String, @Query("limit") limit: Int): Call[ListWrapper[Radio]]

  @GET("/artist/{artist_id}/top")
  def getTopForArtist(@Path("artist_id") artistId: String, @Query("access_token") accessToken: String,
    @Query("limit") limit: Int): Call[ListWrapper[Track]]

  @GET("/search/artist")
  def searchArtist(@Query("access_token") accessToken: String, @Query("limit") limit: Int,
    @Query("q") artistName: String): Call[ListWrapper[Artist]]

}
