package tmdb.list.presentation

import android.app.Application
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.squareup.picasso.Picasso
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.reactivex.functions.Consumer
import tmdb.list.domain.ObserveCoordinator
import tmdb.list.domain.RefreshCoordinator
import tmdb.list.impl.ListItem
import org.jorge.test.tmdb.R
import org.junit.Rule
import org.junit.Test

internal class ListActivityInstrumentation {
  @JvmField
  @Rule
  val activityTestRule = object : ActivityTestRule<ListActivity>(
      ListActivity::class.java, false, false) {
    override fun beforeActivityLaunched() {
      super.beforeActivityLaunched()
      ListActivityKtTestAccessors.componentF { _: Application,
                                               contentView: RecyclerView,
                                               progressView: View,
                                               errorView: View,
                                               guideView: View,
                                               listener: ListViewInteractionListener,
                                               searchView: SearchView ->
        DaggerListActivityInstrumentationComponent.factory()
            .create(
                contentView,
                progressView,
                errorView,
                guideView,
                listener,
                searchView)
      }
    }
  }

  @Test
  fun activityIsShown() {
    every { MOCK_OBSERVE.run(any(), any()) } just Runs
    every { MOCK_REFRESH() } just Runs
    every { MOCK_OBSERVE.abort() } just Runs

    activityTestRule.launchActivity(listActivityIntent(
        InstrumentationRegistry.getInstrumentation().targetContext))

    onView(withId(android.R.id.content)).check(matches(isCompletelyDisplayed()))
  }

  @Test
  fun oneItemIsShown() {
    val posterPath = "holahola"
    every { MOCK_OBSERVE.run(any(), any()) } answers {
      @Suppress("SpellCheckingInspection", "UNCHECKED_CAST")
      (it.invocation.args.first() as Consumer<List<ListItem>>).accept(listOf(ListItem(
          posterPath = posterPath,
          id = 31917,
          name = "Pretty Little Liars")))
    }
    every { MOCK_REFRESH() } just Runs
    every { MOCK_OBSERVE.abort() } just Runs
    // On a whitebox test we would keep track of this mock to asses its verifications, but we're just blackboxing here
    every { MOCK_PICASSO.load("https://image.tmdb.org/t/p/w780/$posterPath") } returns mockk(relaxed = true)

    activityTestRule.launchActivity(listActivityIntent(
        InstrumentationRegistry.getInstrumentation().targetContext))

    onView(withText("Pretty Little Liars")).check(matches(isCompletelyDisplayed()))
  }

  @Test
  fun progressIsShown() {
    every { MOCK_OBSERVE.run(any(), any()) } just Runs
    every { MOCK_REFRESH() } just Runs
    every { MOCK_OBSERVE.abort() } just Runs

    activityTestRule.launchActivity(listActivityIntent(
        InstrumentationRegistry.getInstrumentation().targetContext))

    onView(withId(R.id.progress)).check(matches(isCompletelyDisplayed()))
  }
}

@Component(modules = [
  ContentViewModule::class,
  ListViewConfigModule::class,
  FilterModule::class,
  ConsumerModule::class,
  ListActivityInstrumentationModule::class])
@ListActivityScope
internal interface ListActivityInstrumentationComponent : ListActivityComponent {
  @Component.Factory
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

@Module
internal object ListActivityInstrumentationModule {
  @Provides
  @ListActivityScope
  @JvmStatic
  fun observe() = MOCK_OBSERVE

  @Provides
  @ListActivityScope
  @JvmStatic
  fun refresh() = MOCK_REFRESH

  @Provides
  @ListActivityScope
  @JvmStatic
  fun picasso() = MOCK_PICASSO
}

private val MOCK_OBSERVE = mockk<ObserveCoordinator>()
private val MOCK_REFRESH = mockk<RefreshCoordinator>()
private val MOCK_PICASSO = mockk<Picasso>()
