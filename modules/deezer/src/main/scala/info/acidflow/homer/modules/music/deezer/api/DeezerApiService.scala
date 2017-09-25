package info.acidflow.homer.modules.music.deezer.api

import info.acidflow.homer.modules.music.deezer.api.model.{ListWrapper, Radio, User}
import retrofit2.Call
import retrofit2.http.{GET, Query}


trait DeezerApiService {

    @GET("/user/me")
    def getMyUser(@Query("access_token") accessToken: String): Call[User]

    @GET("/radio/lists")
    def getRadios(@Query("access_token") accessToken: String, @Query("limit") limit : Int): Call[ListWrapper[Radio]]

}
