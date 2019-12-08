package tmdb.list.impl

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
internal object ListApiModule {
  @Provides
  @InitializationContentProviderScope
  @JvmStatic
  fun client(
      clientBuilder: OkHttpClient.Builder,
      interceptor: Interceptor): OkHttpClient = clientBuilder.addInterceptor(interceptor)
      .build()

  @Provides
  @InitializationContentProviderScope
  @JvmStatic
  fun listApi(
      retrofitBuilder: Retrofit.Builder,
      client: OkHttpClient,
      baseUrl: String): ListApi = retrofitBuilder.client(client)
      .baseUrl(baseUrl)
      .build()
      .create(ListApi::class.java)
}
