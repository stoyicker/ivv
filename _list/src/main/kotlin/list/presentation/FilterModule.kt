package list.presentation

import androidx.appcompat.widget.SearchView
import dagger.Module
import dagger.Provides

@Module(includes = [ListViewConfigModule::class])
internal class FilterModule {
  @Provides
  @ListActivityScope
  fun filterDelegate(searchView: SearchView,
                     listViewConfig: ListViewConfig) = FilterDelegate(searchView, listViewConfig)
}
