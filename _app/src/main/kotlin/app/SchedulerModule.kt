package app

import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
internal object SchedulerModule {
  @Provides
  @GlobalScope
  @SchedulerIo
  @JvmStatic
  fun io() = Schedulers.io()

  @Provides
  @GlobalScope
  @SchedulerMain
  @JvmStatic
  fun mainThread() = AndroidSchedulers.mainThread()!!
}
