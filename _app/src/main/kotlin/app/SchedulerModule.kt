package app

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
internal object SchedulerModule {
  @Provides
  @Reusable
  @SchedulerIo
  @JvmStatic
  fun io() = Schedulers.io()

  @Provides
  @Reusable
  @SchedulerMain
  @JvmStatic
  fun mainThread() = AndroidSchedulers.mainThread()!!
}
