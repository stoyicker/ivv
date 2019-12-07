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
object NetworkModule {
  @Provides
  @Singleton
  @JvmStatic
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
  @JvmStatic
  internal fun callAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

  @Provides
  @Singleton
  @Local
  @JvmStatic
  internal fun moshiConverterFactory(): Converter.Factory = MoshiConverterFactory.create()

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class Local
}
