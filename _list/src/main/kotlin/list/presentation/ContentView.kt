package list.presentation

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import org.jorge.test.list.R

/**
 * Wraps UI behavior for top all time gaming posts scenario.
 */
internal class ContentView(
    val recyclerView: RecyclerView,
    val errorView: View,
    private val progressView: View,
    private val guideView: View) : AdditiveLoadableContentView<PresentationItem> {
  override fun showLoadingLayout() {
    pushInfoArea()
    progressView.visibility = View.VISIBLE
    guideView.visibility = View.INVISIBLE
  }

  override fun hideLoadingLayout() {
    progressView.visibility = View.GONE
  }

  override fun addContent(newContent: List<PresentationItem>) {
    (recyclerView.adapter as Adapter).addItems(newContent)
    guideView.visibility = View.VISIBLE
  }

  override fun showErrorLayout() {
    pushInfoArea()
    errorView.visibility = View.VISIBLE
    guideView.visibility = View.INVISIBLE
  }

  override fun hideErrorLayout() {
    errorView.visibility = View.GONE
  }

  private fun pushInfoArea() {
    (recyclerView.layoutParams as FrameLayout.LayoutParams).bottomMargin =
        recyclerView.context.resources.getDimension(R.dimen.footer_padding).toInt()
  }
}
