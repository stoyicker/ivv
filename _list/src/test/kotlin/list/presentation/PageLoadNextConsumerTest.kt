package list.presentation

import io.mockk.mockk
import io.mockk.verify
import list.impl.ListItem
import org.junit.Before
import org.junit.Test

internal class PageLoadNextConsumerTest {
  private val view = mockk<AdditiveLoadableContentView<ListItem>>(relaxUnitFun = true)
  private lateinit var subject: PageLoadNextConsumer

  @Before
  fun setUp() {
    subject = PageLoadNextConsumer(view)
  }

  @Test
  fun accept() {
    val t = mockk<List<ListItem>>()

    subject.accept(t)

    verify {
      view.apply {
        setContent(t)
        hideLoadingLayout()
        hideErrorLayout()
      }
    }
  }
}
