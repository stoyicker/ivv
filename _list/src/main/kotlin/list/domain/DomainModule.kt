package list.domain

import common.Io
import common.MainThread
import common.SchedulerModule
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module(includes = [FunctionalityHolderModule::class, SchedulerModule::class])
internal class DomainModule {
  @Provides
  @Singleton
  fun refresh() = RefreshCoordinator()

  @Provides
  @Singleton
  fun observe(functionalityHolder: FunctionalityHolder,
              @Io ioScheduler: Scheduler,
              @MainThread mainThreadScheduler: Scheduler) = ObserveCoordinator(
      functionalityHolder, ioScheduler, mainThreadScheduler)
}
