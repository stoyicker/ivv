package list.presentation

import dagger.Module
import dagger.Provides

@Module
internal class ConsumerModule {
  @Provides
  @ListActivityScope
  fun onNext(view: ContentView) = PageLoadNextConsumer(view)

  @Provides
  @ListActivityScope
  fun onError(view: ContentView) = PageLoadErrorConsumer(view)
}
