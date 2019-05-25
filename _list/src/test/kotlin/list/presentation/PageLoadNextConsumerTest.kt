package list.presentation

import list.impl.ListItem
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class PageLoadNextConsumerTest {
  private val view = mock(AdditiveLoadableContentView::class.java) as AdditiveLoadableContentView<ListItem>
  private lateinit var subject: PageLoadNextConsumer

  @Before
  fun setUp() {
    subject = PageLoadNextConsumer(view)
  }

  @Test
  fun accept() {
    val t = mock(List::class.java) as List<ListItem>

    subject.accept(t)

    verify(view).addContent(t)
    verify(view).hideLoadingLayout()
    verify(view).hideErrorLayout()
    verifyNoMoreInteractions(t, view)
  }
}
