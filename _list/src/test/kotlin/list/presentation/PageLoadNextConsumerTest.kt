package list.presentation

import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import list.impl.ListItem
import org.junit.After
import org.junit.Test

internal class PageLoadNextConsumerTest {
  private val view = mockk<ContentView>()
  private val subject = PageLoadNextConsumer(view)

  @After
  fun after() = confirmVerified(view)

  @Test
  fun accept() {
    val t = mockk<List<ListItem>>()
    view.apply {
      every { setContent(t) } just Runs
      every { hideLoadingLayout() } just Runs
      every { hideErrorLayout() } just Runs
    }

    subject.accept(t)

    verify {
      view.apply {
        setContent(t)
        hideLoadingLayout()
        hideErrorLayout()
      }
    }
    confirmVerified(t)
  }
}
