package list.impl

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
internal class ListApiModule {
  @Provides
  @InitializationContentProviderScope
  fun client(
      clientBuilder: OkHttpClient.Builder,
      interceptor: Interceptor): OkHttpClient = clientBuilder.addInterceptor(interceptor)
      .build()

  @Provides
  @InitializationContentProviderScope
  fun listApi(
      retrofitBuilder: Retrofit.Builder,
      client: OkHttpClient): ListApi = retrofitBuilder.client(client)
      .baseUrl(BASE_URL)
      .build()
      .create(ListApi::class.java)
}
