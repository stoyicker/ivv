package common

import dagger.Module
import dagger.Provides
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class NetworkModule {
  @Provides
  @Singleton
  fun retrofitBuilder(
      @Local
      callAdapterFactory: CallAdapter.Factory,
      @Local
      moshiConverterFactory: Converter.Factory): Retrofit.Builder = Retrofit.Builder()
      .addCallAdapterFactory(callAdapterFactory)
      .addConverterFactory(moshiConverterFactory)
      .validateEagerly(true)

  @Provides
  @Singleton
  @Local
  internal fun callAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

  @Provides
  @Singleton
  @Local
  internal fun moshiConverterFactory(): Converter.Factory = MoshiConverterFactory.create()

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class Local
}
