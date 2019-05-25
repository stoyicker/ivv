package list.impl

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ListApi {
  @GET("tv/popular")
  fun topRated(@Query(value = "page") page: Int): Single<ResponseBody>
}

internal const val BASE_URL = "https://api.themoviedb.org/3/"
