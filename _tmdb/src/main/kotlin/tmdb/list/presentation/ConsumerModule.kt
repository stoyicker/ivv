package tmdb.list.presentation

import dagger.Module
import dagger.Provides

@Module
object ConsumerModule {
  @Provides
  @ListActivityScope
  @JvmStatic
  fun onNext(view: ContentView) = PageLoadNextConsumer(view)

  @Provides
  @ListActivityScope
  @JvmStatic
  fun onError(view: ContentView) = PageLoadErrorConsumer(view)
}
