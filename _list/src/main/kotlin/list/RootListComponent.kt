package list

import common.ApiKeyInterceptorModule
import dagger.Subcomponent
import list.domain.FunctionalityHolderModule
import list.impl.InitializationContentProviderComponent
import list.presentation.ListActivityComponent
import javax.inject.Singleton

@Subcomponent(modules = [
  FunctionalityHolderModule::class,
  ApiKeyInterceptorModule::class])
@Singleton
interface RootListComponent {
  fun newListActivityComponentFactory(): ListActivityComponent.Factory

  fun newInitializationContentProviderComponent(): InitializationContentProviderComponent
}
