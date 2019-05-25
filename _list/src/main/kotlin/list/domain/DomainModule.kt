package list.domain

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import list.Io
import list.MainThread
import list.SchedulerModule
import javax.inject.Singleton

@Module(includes = [FunctionalityHolderModule::class, SchedulerModule::class])
internal object DomainModule {
  @Provides
  @Singleton
  fun refresh(): IRefreshCoordinator = RefreshCoordinator

  @Provides
  @Singleton
  fun observe(functionalityHolder: FunctionalityHolder,
              @Io ioScheduler: Scheduler,
              @MainThread mainThreadScheduler: Scheduler): IObserveCoordinator =
      ObserveCoordinator(
          functionalityHolder, ioScheduler, mainThreadScheduler)
}
