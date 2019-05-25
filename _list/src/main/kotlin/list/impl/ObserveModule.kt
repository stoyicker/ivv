package list.impl

import dagger.Module
import dagger.Provides
import io.reactivex.subjects.BehaviorSubject
import list.domain.Observe
import javax.inject.Singleton

@Module
internal object ObserveModule {
  @Provides
  @Singleton
  fun pages() = BehaviorSubject.create<ItemPage>()

  @Provides
  @Singleton
  fun observe(pages: BehaviorSubject<ItemPage>): Observe = pages.map { it.items }
}
