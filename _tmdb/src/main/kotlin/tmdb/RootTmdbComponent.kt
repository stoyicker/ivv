package tmdb

import dagger.Subcomponent
import tmdb.list.domain.FunctionalityHolderModule
import tmdb.list.impl.InitializationContentProviderComponent
import tmdb.list.presentation.ListActivityComponent
import javax.inject.Scope

@Subcomponent(modules = [
  FunctionalityHolderModule::class,
  TmdbModule::class])
@TmdbScope
interface RootTmdbComponent {
  fun newListActivityComponentFactory(): ListActivityComponent.Factory

  fun newInitializationContentProviderComponent(): InitializationContentProviderComponent
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class TmdbScope
