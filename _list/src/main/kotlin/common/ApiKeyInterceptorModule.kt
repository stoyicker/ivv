package common

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
internal object ApiKeyInterceptorModule {
  @Provides
  @Singleton
  @JvmStatic
  fun interceptor(): Interceptor = ApiKeyInterceptor()
}
