package list.impl

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import list.NetworkClientModule
import list.NetworkModule
import list.ParserModule
import list.SchedulerModule
import list.domain.FunctionalityHolderModule
import javax.inject.Singleton

@Component(modules = [
  FunctionalityHolderModule::class,
  RefreshModule::class,
  ObserveModule::class])
@Singleton
internal interface InitializationContentProviderComponent {
  fun inject(target: InitializationContentProvider)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder
    fun functionalityHolderModule(functionalityHolderModule: FunctionalityHolderModule): Builder
    fun observeModule(observeModule: ObserveModule): Builder
    fun refreshModule(refreshModule: RefreshModule): Builder
    fun apiKeyInterceptorModule(apiKeyInterceptorModule: ApiKeyInterceptorModule): Builder
    fun listApiModule(listApiModule: ListApiModule): Builder
    fun networkClientModule(networkClientModule: NetworkClientModule): Builder
    fun networkModule(networkModule: NetworkModule): Builder
    fun parserModule(parserModule: ParserModule): Builder
    fun schedulerModule(schedulerModule: SchedulerModule): Builder
    fun build(): InitializationContentProviderComponent
  }
}
