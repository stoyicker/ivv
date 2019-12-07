package list

import android.content.Context
import common.ApiKeyInterceptorModule
import common.FileSystemModule
import common.NetworkClientModule
import common.NetworkModule
import common.ParserModule
import common.PicassoModule
import common.SchedulerModule
import dagger.BindsInstance
import dagger.Component
import list.domain.FunctionalityHolderModule
import list.impl.InitializationContentProviderComponent
import list.presentation.ListActivityComponent
import javax.inject.Singleton

@Component(modules = [
  FunctionalityHolderModule::class,
  PicassoModule::class,
  SchedulerModule::class,
  NetworkModule::class,
  NetworkClientModule::class,
  ApiKeyInterceptorModule::class,
  ParserModule::class,
  FileSystemModule::class])
@Singleton
internal interface RootListComponent {
  fun listActivityComponentFactory(): ListActivityComponent.Factory

  fun initializationContentProviderComponent(): InitializationContentProviderComponent

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): RootListComponent
  }
}

internal object RootListComponentHolder {
  lateinit var rootListComponent: RootListComponent

  fun init(context: Context) {
    rootListComponent = DaggerRootListComponent.factory()
        .create(context)
  }
}
