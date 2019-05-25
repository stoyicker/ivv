package list.presentation

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import dagger.BindsInstance
import dagger.Component
import list.SchedulerModule
import list.domain.DomainModule
import list.domain.FunctionalityHolderModule
import javax.inject.Qualifier
import javax.inject.Singleton

@Component(modules = [
  ContentViewModule::class,
  ListViewConfigModule::class,
  FilterModule::class,
  ConsumerModule::class,
  DomainModule::class])
@Singleton
internal interface ListActivityComponent {
  fun inject(target: ListActivity)

  @Component.Builder
  interface Builder {
    fun contentViewModule(contentViewModule: ContentViewModule): Builder
    fun filterModule(filterModule: FilterModule): Builder
    fun functionalityHolderModule(functionalityHolderModule: FunctionalityHolderModule): Builder
    fun domainModule(domainModule: DomainModule): Builder
    fun schedulerModule(schedulerModule: SchedulerModule): Builder
    fun consumerModule(consumerModule: ConsumerModule): Builder
    @BindsInstance
    fun contentView(contentView: RecyclerView): Builder
    @BindsInstance
    fun progressView(@Progress progressView: View): Builder
    @BindsInstance
    fun errorView(@Error errorView: View): Builder
    @BindsInstance
    fun guideView(@Guide guideView: View): Builder
    @BindsInstance
    fun listViewInteractionListener(listViewInteractionListener: ListViewInteractionListener): Builder
    @BindsInstance
    fun searchView(searchView: SearchView): Builder
    fun build(): ListActivityComponent
  }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class Progress

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class Error

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class Guide
