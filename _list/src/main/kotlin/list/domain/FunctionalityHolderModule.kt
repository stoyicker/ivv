package list.domain

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class FunctionalityHolderModule {
  @Provides
  @Singleton
  fun functionalityHolder() = FunctionalityHolder()
}
