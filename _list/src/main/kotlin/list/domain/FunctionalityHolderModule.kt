package list.domain

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object FunctionalityHolderModule {
  @Provides
  @Singleton
  @JvmStatic
  fun functionalityHolder() = FunctionalityHolder()
}
