package list.domain

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.processors.PublishProcessor
import list.Io
import list.MainThread
import list.SchedulerModule
import javax.inject.Singleton

@Module(includes = [SchedulerModule::class])
internal object DomainModule {
  @Provides
  @Singleton
  fun functionalityHolder() = FunctionalityHolder()

  @Provides
  fun valve() = PublishProcessor.create<Boolean>()

  @Provides
  @Singleton
  fun observe(functionalityHolder: FunctionalityHolder,
              @Io ioScheduler: Scheduler,
              @MainThread mainThreadScheduler: Scheduler,
              valve: PublishProcessor<Boolean>) =
      ObserveCoordinator(functionalityHolder, ioScheduler, mainThreadScheduler, valve)

  @Provides
  @Singleton
  fun dispatch(functionalityHolder: FunctionalityHolder) = DispatchCoordinator(functionalityHolder)
}
