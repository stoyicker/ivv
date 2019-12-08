package tmdb.list.presentation

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import tmdb.list.impl.ListItem
import org.jorge.test.tmdb.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ContentViewTest {
  private val recyclerView = mockk<RecyclerView>()
  private val errorView = mockk<View>()
  private val progressView = mockk<View>()
  private val guideView = mockk<View>()
  private val subject = ContentView(recyclerView, errorView, progressView, guideView)

  @After
  fun tearDown() = confirmVerified(recyclerView, errorView, progressView, guideView)

  @Test
  fun showLoadingLayout() {
    val dimension = -83F
    val mockResources = mockk<Resources> {
      every { getDimension(R.dimen.footer_padding) } returns dimension
    }
    val mockContext = mockk<Context> {
      every { resources } returns mockResources
    }
    val mockLayoutParams = mockk<FrameLayout.LayoutParams>()
    recyclerView.apply {
      every { context } returns mockContext
      every { layoutParams } returns mockLayoutParams
    }
    every { progressView.visibility = View.VISIBLE } just Runs
    every { guideView.visibility = View.INVISIBLE } just Runs

    subject.showLoadingLayout()

    verify(exactly = 1) {
      recyclerView.apply {
        layoutParams
        context
      }
      mockContext.resources
      mockResources.getDimension(R.dimen.footer_padding)
      progressView.visibility = View.VISIBLE
      guideView.visibility = View.INVISIBLE
    }
    confirmVerified(mockResources, mockContext, mockLayoutParams)
    assertEquals(dimension.toInt(), mockLayoutParams.bottomMargin)
  }

  @Test
  fun hideLoadingLayout() {
    every { progressView.visibility = View.GONE } just Runs

    subject.hideLoadingLayout()

    verify(exactly = 1) {
      progressView.visibility = View.GONE
    }
  }

  @Test
  fun setContent() {
    val dimension = -83F
    val mockResources = mockk<Resources> {
      every { getDimension(R.dimen.footer_padding) } returns dimension
    }
    val mockContext = mockk<Context> {
      every { resources } returns mockResources
    }
    val mockLayoutParams = mockk<FrameLayout.LayoutParams>()
    val newContent = mockk<List<ListItem>>()
    val mockAdapter = mockk<Adapter> {
      every { setItems(newContent) } just Runs
    }
    recyclerView.apply {
      every { context } returns mockContext
      every { layoutParams } returns mockLayoutParams
      every { adapter } returns mockAdapter
    }
    every { guideView.visibility = View.VISIBLE } just Runs

    subject.setContent(newContent)

    verify(exactly = 1) {
      recyclerView.apply {
        layoutParams
        context
        adapter
      }
      mockContext.resources
      mockResources.getDimension(R.dimen.footer_padding)
      mockAdapter.setItems(newContent)
      guideView.visibility = View.VISIBLE
    }
    confirmVerified(mockResources, mockContext, mockLayoutParams, newContent, mockAdapter)
    assertEquals(dimension.toInt(), mockLayoutParams.bottomMargin)
  }

  @Test
  fun showErrorLayout() {
    val dimension = -83F
    val mockResources = mockk<Resources> {
      every { getDimension(R.dimen.footer_padding) } returns dimension
    }
    val mockContext = mockk<Context> {
      every { resources } returns mockResources
    }
    val mockLayoutParams = mockk<FrameLayout.LayoutParams>()
    recyclerView.apply {
      every { context } returns mockContext
      every { layoutParams } returns mockLayoutParams
    }
    every { errorView.visibility = View.VISIBLE } just Runs
    every { guideView.visibility = View.INVISIBLE } just Runs

    subject.showErrorLayout()

    verify(exactly = 1) {
      recyclerView.apply {
        layoutParams
        context
      }
      mockContext.resources
      mockResources.getDimension(R.dimen.footer_padding)
      errorView.visibility = View.VISIBLE
      guideView.visibility = View.INVISIBLE
    }
    confirmVerified(mockResources, mockContext, mockLayoutParams)
    assertEquals(dimension.toInt(), mockLayoutParams.bottomMargin)
  }

  @Test
  fun hideErrorLayout() {
    every { errorView.visibility = View.GONE } just Runs

    subject.hideErrorLayout()

    verify(exactly = 1) {
      errorView.visibility = View.GONE
    }
  }
}
