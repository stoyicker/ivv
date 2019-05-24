package list.presentation

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class ListViewConfigModule {
  @Provides
  @Singleton
  fun listViewConfig(listener: ListViewInteractionListener) = ListViewConfig(listener)
}
