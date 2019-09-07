package list.presentation

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.reactivex.functions.Consumer
import list.domain.IObserveCoordinator
import list.domain.IRefreshCoordinator
import list.impl.ListItem
import list.presentation.ListActivityKtTestAccessors.componentF
import org.jorge.test.list.R
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

internal class ListActivityInstrumentation {
  @JvmField
  @Rule
  val activityTestRule = object : ActivityTestRule<ListActivity>(
      ListActivity::class.java, false, false) {
    override fun beforeActivityLaunched() {
      super.beforeActivityLaunched()
      componentF { contentView: RecyclerView,
                   progressView: View,
                   errorView: View,
                   guideView: View,
                   listener: ListViewInteractionListener,
                   searchView: SearchView ->
        DaggerListActivityInstrumentationComponent.builder()
            .contentViewModule(ContentViewModule)
            .filterModule(FilterModule)
            .contentView(contentView)
            .listActivityInstrumentationModule(ListActivityInstrumentationModule)
            .progressView(progressView)
            .errorView(errorView)
            .consumerModule(ConsumerModule)
            .guideView(guideView)
            .listViewInteractionListener(listener)
            .searchView(searchView)
            .build() as ListActivityInstrumentationComponent
      }
    }
  }

  @Test
  fun activityIsShown() {
    every { MOCK_OBSERVE.run(any(), any()) } just Runs
    every { MOCK_REFRESH.run() } just Runs
    every { MOCK_OBSERVE.abort() } just Runs
    activityTestRule.launchActivity(listActivityIntent(InstrumentationRegistry.getTargetContext()))
    onView(withId(android.R.id.content)).check(matches(isCompletelyDisplayed()))
  }

  @Test
  fun oneItemIsShown() {
    every { MOCK_OBSERVE.run(any(), any()) } answers {
      @Suppress("SpellCheckingInspection", "UNCHECKED_CAST")
      (it.invocation.args.first() as Consumer<List<ListItem>>).accept(listOf(ListItem(
          posterPath = "/vC324sdfcS313vh9QXwijLIHPJp.jpg",
          id = 31917,
          name = "Pretty Little Liars")))
    }
    every { MOCK_REFRESH.run() } just Runs
    every { MOCK_OBSERVE.abort() } just Runs
    activityTestRule.launchActivity(listActivityIntent(InstrumentationRegistry.getTargetContext()))
    onView(withText("Pretty Little Liars")).check(matches(isCompletelyDisplayed()))
  }

  @Test
  fun progressIsShown() {
    every { MOCK_OBSERVE.run(any(), any()) } just Runs
    every { MOCK_REFRESH.run() } just Runs
    every { MOCK_OBSERVE.abort() } just Runs
    activityTestRule.launchActivity(listActivityIntent(InstrumentationRegistry.getTargetContext()))
    onView(withId(R.id.progress)).check(matches(isCompletelyDisplayed()))
  }
}

@Component(modules = [
  ContentViewModule::class,
  ListViewConfigModule::class,
  FilterModule::class,
  ConsumerModule::class,
  ListActivityInstrumentationModule::class])
@Singleton
internal interface ListActivityInstrumentationComponent : ListActivityComponent {
  @Component.Builder
  interface Builder {
    fun contentViewModule(contentViewModule: ContentViewModule): Builder
    fun listViewConfigModule(listViewConfigModule: ListViewConfigModule): Builder
    fun filterModule(filterModule: FilterModule): Builder
    fun consumerModule(consumerModule: ConsumerModule): Builder
    fun listActivityInstrumentationModule(listActivityInstrumentationModule: ListActivityInstrumentationModule): Builder
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

@Module
internal object ListActivityInstrumentationModule {
  @Provides
  @Singleton
  fun observe() = MOCK_OBSERVE

  @Provides
  @Singleton
  fun refresh() = MOCK_REFRESH
}

private val MOCK_OBSERVE = mockk<IObserveCoordinator>()
private val MOCK_REFRESH = mockk<IRefreshCoordinator>()
