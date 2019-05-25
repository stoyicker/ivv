package list.domain

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import list.Io
import list.MainThread
import list.SchedulerModule
import javax.inject.Singleton

@Module(includes = [FunctionalityHolderModule::class, SchedulerModule::class])
internal object DomainModule {
  @Provides
  fun valve(): Subject<Boolean> = PublishSubject.create<Boolean>()

  @Provides
  @Singleton
  fun refresh(functionalityHolder: FunctionalityHolder): IRefreshCoordinator
      = RefreshCoordinator(functionalityHolder)

  @Provides
  @Singleton
  fun observe(functionalityHolder: FunctionalityHolder,
              @Io ioScheduler: Scheduler,
              @MainThread mainThreadScheduler: Scheduler,
              valve: Subject<Boolean>,
              refreshCoordinator: IRefreshCoordinator): IObserveCoordinator =
      ObserveCoordinator(
          functionalityHolder, ioScheduler, mainThreadScheduler, valve, refreshCoordinator)
}
