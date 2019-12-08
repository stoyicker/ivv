package tmdb.list.domain

import dagger.Module
import dagger.Provides
import tmdb.TmdbScope

@Module
internal object FunctionalityHolderModule {
  @Provides
  @TmdbScope
  @JvmStatic
  fun functionalityHolder() = FunctionalityHolder()
}
