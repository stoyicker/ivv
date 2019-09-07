package common

import dagger.Module
import dagger.Provides
import org.jorge.test.common.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {
  @Provides
  @Singleton
  fun retrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .validateEagerly(BuildConfig.DEBUG)
}
