package app

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
internal object NetworkModule {
  @Provides
  @JvmStatic
  fun client() = OkHttpClient.Builder()
      .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

  @Provides
  @GlobalScope
  @JvmStatic
  fun retrofitBuilder(
      @LocalNetworkModule
      callAdapterFactory: CallAdapter.Factory,
      @LocalNetworkModule
      converterFactory: Converter.Factory): Retrofit.Builder = Retrofit.Builder()
      .addCallAdapterFactory(callAdapterFactory)
      .addConverterFactory(converterFactory)
      .validateEagerly(true)

  @Provides
  @GlobalScope
  @LocalNetworkModule
  @JvmStatic
  fun callAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

  @Provides
  @GlobalScope
  @LocalNetworkModule
  @JvmStatic
  fun moshiConverterFactory(): Converter.Factory = MoshiConverterFactory.create()

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class LocalNetworkModule
}

private const val TIMEOUT_SECONDS = 15L
