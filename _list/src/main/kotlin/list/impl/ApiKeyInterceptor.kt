package list.impl

import okhttp3.Interceptor
import org.jorge.test.list.BuildConfig

internal class ApiKeyInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain) =
      chain.proceed(with(chain.request()) {
        newBuilder().url(url().newBuilder().addQueryParameter("api_key", API_KEY).build()).build()
      })
}

private val API_KEY = BuildConfig.API_KEY
