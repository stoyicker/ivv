package common

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ParserModule {
  @Provides
  @Singleton
  fun moshiBuilder() = Moshi.Builder()
}
