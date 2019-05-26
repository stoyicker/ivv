package list.presentation

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import list.impl.ListItem
import org.jorge.test.list.R

internal class ContentView(
    val recyclerView: RecyclerView,
    val errorView: View,
    private val progressView: View,
    private val guideView: View) : AdditiveLoadableContentView<ListItem> {
  override fun showLoadingLayout() {
    pushInfoArea()
    progressView.visibility = View.VISIBLE
    guideView.visibility = View.INVISIBLE
  }

  override fun hideLoadingLayout() {
    progressView.visibility = View.GONE
  }

  override fun setContent(newContent: List<ListItem>) {
    (recyclerView.adapter as Adapter).setItems(newContent)
    pushInfoArea()
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
