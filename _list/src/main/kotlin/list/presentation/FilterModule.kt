package list.presentation

import android.support.v7.widget.SearchView
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ListViewConfigModule::class])
internal object FilterModule {
  @Provides
  @Singleton
  fun filterDelegate(searchView: SearchView,
                     listViewConfig: ListViewConfig) = FilterDelegate(searchView, listViewConfig)
}
