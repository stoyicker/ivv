package tmdb.list.impl

import dagger.Module
import dagger.Provides
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject

@Module
internal object ObserveModule {
  @Provides
  @InitializationContentProviderScope
  @JvmStatic
  fun observe(impl: BehaviorSubject<List<ListItem>>) =
      impl.toFlowable(BackpressureStrategy.LATEST)

  @Provides
  @JvmStatic
  @InitializationContentProviderScope
  fun pages() = BehaviorSubject.create<List<ListItem>>()
}
