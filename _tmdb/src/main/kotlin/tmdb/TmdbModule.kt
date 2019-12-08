package tmdb

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor

@Module
internal object TmdbModule {
  @Provides
  @TmdbScope
  @JvmStatic
  fun interceptor(): Interceptor = ApiKeyInterceptor()

  @Provides
  @TmdbScope
  @JvmStatic
  fun baseUrl() = BASE_URL
}

private const val BASE_URL = "https://api.themoviedb.org/3/"
