package tmdb.list.presentation

import androidx.appcompat.widget.SearchView
import dagger.Module
import dagger.Provides

@Module(includes = [ListViewConfigModule::class])
class FilterModule {
  @Provides
  @ListActivityScope
  fun filterDelegate(searchView: SearchView,
                     listViewConfig: ListViewConfig) = FilterDelegate(searchView, listViewConfig)
}
