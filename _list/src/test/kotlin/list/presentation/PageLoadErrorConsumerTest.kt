package list.presentation

import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Test

internal class PageLoadErrorConsumerTest {
  private val contentView = mockk<ContentView>()
  private val subject = PageLoadErrorConsumer(contentView)

  @After
  fun tearDown() = confirmVerified(contentView)

  @Test
  fun accept() {
    val throwable = mockk<Throwable>()
    contentView.apply {
      every { showErrorLayout() } just Runs
      every { hideLoadingLayout() } just Runs
    }

    subject.accept(throwable)

    verify(exactly = 1){
      contentView.apply {
        showErrorLayout()
        hideLoadingLayout()
      }
    }
    confirmVerified(throwable)
  }
}
