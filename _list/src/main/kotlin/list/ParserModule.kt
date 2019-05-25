package list

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object ParserModule {
  @Provides
  @Singleton
  fun moshiBuilder() = Moshi.Builder()
}
