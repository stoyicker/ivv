package list.impl

import android.content.Context
import dagger.BindsInstance
import dagger.Component
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

    fun refreshModule(refreshModule: RefreshModule): Builder

    fun build(): InitializationContentProviderComponent
  }
}
