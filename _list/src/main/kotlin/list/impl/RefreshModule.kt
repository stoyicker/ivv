package list.impl

import app.SchedulerIo
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject

@Module(includes = [RefreshSourceModule::class])
internal object RefreshModule {
  @Provides
  @InitializationContentProviderScope
  @JvmStatic
  fun refresh(refreshSource: RefreshSource,
              @SchedulerIo scheduler: Scheduler,
              truthSource: BehaviorSubject<List<ListItem>>) =
      Refresh(refreshSource, scheduler, truthSource)
}
