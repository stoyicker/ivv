package common

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkClientModule {
  @Provides
  @Singleton
  fun client(): OkHttpClient.Builder = OkHttpClient.Builder()
      .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
}

private const val TIMEOUT_SECONDS = 15L
