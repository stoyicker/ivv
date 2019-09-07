package list.impl

import dagger.Subcomponent

@Subcomponent(modules = [
  RefreshModule::class,
  ObserveModule::class])
@InitializationContentProviderScope
internal interface InitializationContentProviderComponent {
  fun inject(target: InitializationContentProvider)

  @Subcomponent.Builder
  interface Builder {
    fun refreshModule(refreshModule: RefreshModule): Builder

    fun build(): InitializationContentProviderComponent
  }
}
