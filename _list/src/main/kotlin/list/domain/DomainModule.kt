package list.domain

import app.SchedulerIo
import app.SchedulerMain
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import list.presentation.ListActivityScope

@Module
internal object DomainModule {
  @Provides
  @ListActivityScope
  @JvmStatic
  fun refresh(functionalityHolder: FunctionalityHolder) = RefreshCoordinator(functionalityHolder)

  @Provides
  @ListActivityScope
  @JvmStatic
  fun observe(functionalityHolder: FunctionalityHolder,
              @SchedulerIo ioScheduler: Scheduler,
              @SchedulerMain mainThreadScheduler: Scheduler) = ObserveCoordinator(
      functionalityHolder, ioScheduler, mainThreadScheduler)
}
