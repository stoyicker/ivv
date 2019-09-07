package list.presentation

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
internal class ListViewConfigModule {
  @Provides
  @ListActivityScope
  fun listViewConfig(
      picasso: Picasso,
      listener: ListViewInteractionListener) = ListViewConfig(picasso, listener)
}
