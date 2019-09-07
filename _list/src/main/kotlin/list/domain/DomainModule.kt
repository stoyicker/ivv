package list.domain

import common.Io
import common.MainThread
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import list.presentation.ListActivityScope

@Module
internal class DomainModule {
  @Provides
  @ListActivityScope
  fun refresh() = RefreshCoordinator()

  @Provides
  @ListActivityScope
  fun observe(functionalityHolder: FunctionalityHolder,
              @Io ioScheduler: Scheduler,
              @MainThread mainThreadScheduler: Scheduler) = ObserveCoordinator(
      functionalityHolder, ioScheduler, mainThreadScheduler)
}
