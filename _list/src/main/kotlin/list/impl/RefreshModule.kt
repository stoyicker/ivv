package list.impl

import common.Io
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject

@Module(includes = [RefreshSourceModule::class])
internal class RefreshModule {
  @Provides
  @InitializationContentProviderScope
  fun refresh(refreshSource: RefreshSource,
              @Io scheduler: Scheduler,
              truthSource: BehaviorSubject<List<ListItem>>) = Refresh(
      refreshSource, scheduler, truthSource)
}
