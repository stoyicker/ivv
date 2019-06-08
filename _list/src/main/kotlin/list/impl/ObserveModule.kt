package list.impl

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Singleton

@Module
internal object ObserveModule {
  @Provides
  @Singleton
  fun pages() = BehaviorSubject.create<List<ListItem>>()

  @Provides
  @Singleton
  fun observe(impl: BehaviorSubject<List<ListItem>>): Observable<List<ListItem>> = impl
}
