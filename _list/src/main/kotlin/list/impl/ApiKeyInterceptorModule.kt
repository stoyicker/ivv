package list.impl

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
internal object ApiKeyInterceptorModule {
  @Provides
  @Singleton
  fun interceptor(): Interceptor = ApiKeyInterceptor()
}
