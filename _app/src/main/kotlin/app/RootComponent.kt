package app

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import list.RootListComponent
import splash.RootSplashComponent
import javax.inject.Scope

@Component(modules = [
  FileSystemModule::class,
  NetworkModule::class,
  ParserModule::class,
  PicassoModule::class,
  SchedulerModule::class])
@GlobalScope
internal interface RootComponent {
  fun newRootSplashComponent(): RootSplashComponent

  fun newRootListComponent(): RootListComponent

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): RootComponent
  }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class GlobalScope
