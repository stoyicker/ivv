package list.impl

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import list.Io
import list.SchedulerModule
import list.domain.Refresh
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
               truthSource: BehaviorSubject<List<ListItem>>): Refresh  =
      RefreshImpl(refreshSource, scheduler, truthSource)
}
