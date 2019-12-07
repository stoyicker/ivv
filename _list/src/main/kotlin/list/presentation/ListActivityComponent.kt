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

  @Subcomponent.Factory
  interface Factory {
    fun create(
        @BindsInstance contentView: RecyclerView,
        @BindsInstance @Progress progressView: View,
        @BindsInstance @Error errorView: View,
        @BindsInstance @Guide guideView: View,
        @BindsInstance listViewInteractionListener: ListViewInteractionListener,
        @BindsInstance searchView: SearchView): ListActivityComponent
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
