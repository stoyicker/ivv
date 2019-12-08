package tmdb.list.domain

import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import tmdb.list.domain.RefreshCoordinatorTestAccessors.nextPage
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

internal class RefreshCoordinatorTest {
  private val functionalityHolder = mockk<FunctionalityHolder>()
  private val subject = RefreshCoordinator(functionalityHolder)

  @After
  fun tearDown() {
    confirmVerified(functionalityHolder)
  }

  @Test
  operator fun invoke() {
    val currentPage: Int = subject.nextPage()
    every { functionalityHolder.refresh(currentPage) } just Runs

    subject()

    verify(exactly = 1) {
      functionalityHolder.refresh(currentPage)
    }
    assertEquals(currentPage + 1, subject.nextPage())
  }
}
