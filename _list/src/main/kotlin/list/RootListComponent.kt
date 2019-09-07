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
  fun listActivityComponent(): ListActivityComponent.Builder

  fun initializationContentProviderComponent(): InitializationContentProviderComponent.Builder

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder

    fun build(): RootListComponent
  }
}

internal object RootListComponentHolder {
  lateinit var rootListComponent: RootListComponent

  fun init(context: Context) {
    rootListComponent = DaggerRootListComponent.builder()
        .context(context)
        .build()
  }
}
