package list.impl

import common.ApiKeyInterceptorModule
import common.NetworkClientModule
import common.NetworkModule
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [
  NetworkClientModule::class, ApiKeyInterceptorModule::class, NetworkModule::class])
internal class ListApiModule {
  @Provides
  @Singleton
  fun client(
      clientBuilder: OkHttpClient.Builder,
      interceptor: Interceptor): OkHttpClient = clientBuilder.addInterceptor(interceptor)
      .build()

  @Provides
  @Singleton
  fun listApi(
      retrofitBuilder: Retrofit.Builder,
      client: OkHttpClient): ListApi = retrofitBuilder.client(client)
      .baseUrl(BASE_URL)
      .build()
      .create(ListApi::class.java)
}
