package list.impl

import com.nytimes.android.external.store3.base.impl.Store
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

internal class RefreshSourceTest {
  private val store = mockk<Store<RefreshResponse, Int>>()
  private val storeAccessor = mockk<dagger.Lazy<Store<RefreshResponse, Int>>>() {
    every { get() } returns store
  }
  private val subject = RefreshSource(storeAccessor)

  @Before
  fun setUp() {
    // Consume this call since it happens in the constructor super() invocation
    verify(exactly = 1) {
      storeAccessor.get()
    }
  }

  @After
  fun tearDown() {
    confirmVerified(store, storeAccessor)
  }

  @Test
  operator fun invoke() {
    val parameters = 0
    val expected = mockk<Single<RefreshResponse>>()
    every { store.get(parameters) } returns expected

    val actual = subject(parameters)

    verify(exactly = 1) {
      store.get(parameters)
    }
    confirmVerified(expected)
    assertSame(expected, actual)
  }
}
