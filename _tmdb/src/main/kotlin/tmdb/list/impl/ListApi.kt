package tmdb.list.impl

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ListApi {
  @GET("tv/popular")
  fun tvPopular(@Query(value = "page") page: Int): Single<ResponseBody>
}
