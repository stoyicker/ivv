package list.presentation

import android.R
import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import list.domain.FunctionalityHolder
import list.domain.Observe
import list.domain.Refresh
import list.impl.InitializationContentProviderComponent
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import javax.inject.Singleton

internal class ListActivityInstrumentation {
  @JvmField
  @Rule
  val activityTestRule = object : ActivityTestRule<ListActivity>(
      ListActivity::class.java, false, true) {
    // Here we inject a function that will provide a component that returns stubbed data
    // so our tests don't need to depend on the network
    override fun beforeActivityLaunched() {
      list.impl.componentF = { _: Context ->
        DaggerInitializationContentProviderInstrumentationComponent.builder()
            .initializationContentProviderInstrumentationModule(InitializationContentProviderInstrumentationModule)
            .build()
      }
    }
  }

  @Test
  fun activityIsShown() {
    onView(withId(R.id.content)).check(matches(isCompletelyDisplayed()))
  }
}

@Component(modules = [InitializationContentProviderInstrumentationModule::class])
@Singleton
internal interface InitializationContentProviderInstrumentationComponent
  : InitializationContentProviderComponent

@Module
internal object InitializationContentProviderInstrumentationModule {
  @Provides
  @Singleton
  fun functionalityHolder() = MOCK_FUNCTIONALITY_HOLDER

  @Provides
  @Singleton
  fun observe() = MOCK_OBSERVE

  @Provides
  @Singleton
  fun refresh() = MOCK_REFRESH
}

private val MOCK_FUNCTIONALITY_HOLDER = mock(FunctionalityHolder::class.java)!!
private val MOCK_OBSERVE = mock(Observable::class.java)!! as Observe
private val MOCK_REFRESH = mock(Refresh::class.java)!!
