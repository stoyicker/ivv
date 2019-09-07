package list.presentation

import com.squareup.picasso.Picasso
import common.PicassoModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [PicassoModule::class])
internal class ListViewConfigModule {
  @Provides
  @Singleton
  fun listViewConfig(
      picasso: Picasso,
      listener: ListViewInteractionListener) = ListViewConfig(picasso, listener)
}
