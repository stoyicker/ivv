package list.presentation

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import dagger.BindsInstance
import dagger.Subcomponent
import list.domain.DomainModule
import javax.inject.Qualifier

@Subcomponent(modules = [
  ContentViewModule::class,
  ListViewConfigModule::class,
  FilterModule::class,
  ConsumerModule::class,
  DomainModule::class])
@ListActivityScope
internal interface ListActivityComponent {
  fun inject(target: ListActivity)

  @Subcomponent.Builder
  interface Builder {
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
