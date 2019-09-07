package common

import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
object SchedulerModule {
  @Provides
  @Singleton
  @Io
  fun io() = Schedulers.io()

  @Provides
  @Singleton
  @MainThread
  fun mainThread() = AndroidSchedulers.mainThread()!!
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Io

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainThread
