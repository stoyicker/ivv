package app

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides

@Module
internal object ParserModule {
  @Provides
  @GlobalScope
  @JvmStatic
  fun moshiBuilder() = Moshi.Builder()
}
