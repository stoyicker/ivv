package list.presentation

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
internal object ListViewConfigModule {
  @Provides
  @ListActivityScope
  @JvmStatic
  fun listViewConfig(
      picasso: Picasso,
      listener: ListViewInteractionListener) = ListViewConfig(picasso, listener)
}
