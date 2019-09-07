package list.impl

import common.Io
import common.SchedulerModule
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Singleton

@Module(includes = [
  RefreshSourceModule::class,
  SchedulerModule::class,
  ObserveModule::class])
internal object RefreshModule {
  @Provides
  @Singleton
  fun refresh(refreshSource: RefreshSource,
              @Io scheduler: Scheduler,
              truthSource: BehaviorSubject<List<ListItem>>) = Refresh(
      refreshSource, scheduler, truthSource)
}
