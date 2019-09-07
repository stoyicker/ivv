package list.impl

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

@Module
internal class ObserveModule {
  @Provides
  @InitializationContentProviderScope
  fun pages() = BehaviorSubject.create<List<ListItem>>()

  @Provides
  @InitializationContentProviderScope
  fun observe(impl: BehaviorSubject<List<ListItem>>): Observable<List<ListItem>> = impl
}
