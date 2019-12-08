package tmdb.list.presentation

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import dagger.BindsInstance
import dagger.Subcomponent
import tmdb.list.domain.DomainModule
import javax.inject.Qualifier

@Subcomponent(modules = [
  ContentViewModule::class,
  ListViewConfigModule::class,
  FilterModule::class,
  ConsumerModule::class,
  DomainModule::class])
@ListActivityScope
interface ListActivityComponent {
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
annotation class Progress

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Error

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Guide
