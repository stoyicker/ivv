package common

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
class ApiKeyInterceptorModule {
  @Provides
  @Singleton
  fun interceptor(): Interceptor = ApiKeyInterceptor()
}
