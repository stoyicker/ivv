package tmdb

import okhttp3.Interceptor
import okhttp3.Response
import org.jorge.test.common.BuildConfig

internal class ApiKeyInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response =
      chain.proceed(with(chain.request()) {
        newBuilder().url(url().newBuilder()
            .addQueryParameter(QUERY_PARAM_KEY_API_KEY, API_KEY)
            .build())
            .build()
      })
}

private const val QUERY_PARAM_KEY_API_KEY = "api_key"
private const val API_KEY = BuildConfig.API_KEY
