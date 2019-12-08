package tmdb.list.impl

import dagger.Subcomponent

@Subcomponent(modules = [
  RefreshModule::class,
  ObserveModule::class])
@InitializationContentProviderScope
interface InitializationContentProviderComponent {
  fun inject(target: InitializationContentProvider)
}
