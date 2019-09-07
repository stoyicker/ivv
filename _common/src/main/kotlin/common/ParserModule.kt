package common

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ParserModule {
  @Provides
  @Singleton
  fun moshiBuilder() = Moshi.Builder()
}
