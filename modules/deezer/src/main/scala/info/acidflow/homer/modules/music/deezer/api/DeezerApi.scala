package info.acidflow.homer.modules.music.deezer.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import info.acidflow.homer.modules.music.deezer.api.model.{Radio, User}
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

import scala.concurrent.{ExecutionContext, Future}


class DeezerApi(private[this] var baseUrl: String, private[this] val accessToken: String) {

  private[this] val service = new Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper().registerModule(DefaultScalaModule)))
    .build()
    .create(classOf[DeezerApiService])

  def getMyUser()(implicit ec: ExecutionContext): Future[User] = {
    Future {
      service.getMyUser(accessToken).execute().body()
    }
  }

  def getRadios()(implicit ec: ExecutionContext): Future[List[Radio]] = {
    Future {
      service.getRadios(accessToken, 1000).execute().body().data
    }
  }


}
