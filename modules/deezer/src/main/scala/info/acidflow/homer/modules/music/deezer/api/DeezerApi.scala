package info.acidflow.homer.modules.music.deezer.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.modules.music.deezer.api.model.{Artist, Radio, Track}
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

import scala.concurrent.{ExecutionContext, Future}


class DeezerApi(private[this] var baseUrl: String, private[this] val accessToken: String) extends LazyLogging {

  private[this] val service = new Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper().registerModule(DefaultScalaModule)))
    .build()
    .create(classOf[DeezerApiService])

  def getMyUser()(implicit ec: ExecutionContext): Future[Artist] = {
    synchronized {
      Future {
        service.getMyUser(accessToken).execute().body()
      }
    }
  }

  def getRadios()(implicit ec: ExecutionContext): Future[List[Radio]] = {
    synchronized {
      Future {
        service.getRadios(accessToken, 1000).execute().body().data
      }
    }
  }

  def searchArtist(artistName: String)(implicit ec: ExecutionContext): Future[Artist] = {
    Future {
      logger.info("Artist slot: {}", artistName)
      service.searchArtist(accessToken, 1, artistName).execute().body().data.head
    }
  }

  def getTopForArtist(artistId: String)(implicit ec: ExecutionContext): Future[List[Track]] = {
    Future {
      service.getTopForArtist(artistId, accessToken, 150).execute().body().data
    }
  }


}
