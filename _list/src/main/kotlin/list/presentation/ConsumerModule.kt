package list.presentation

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object ConsumerModule {
  @Provides
  @Singleton
  fun onNext(view: ContentView) = PageLoadNextConsumer(view)

  @Provides
  @Singleton
  fun onError(view: ContentView) = PageLoadErrorConsumer(view)
}
