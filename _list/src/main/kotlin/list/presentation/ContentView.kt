package list.presentation

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import list.impl.ListItem
import org.jorge.test.list.R

internal class ContentView(
    val recyclerView: RecyclerView,
    val errorView: View,
    private val progressView: View,
    private val guideView: View) {
  fun showLoadingLayout() {
    pushInfoArea()
    progressView.visibility = View.VISIBLE
    guideView.visibility = View.INVISIBLE
  }

  fun hideLoadingLayout() {
    progressView.visibility = View.GONE
  }

  fun setContent(newContent: List<ListItem>) {
    (recyclerView.adapter as Adapter).setItems(newContent)
    pushInfoArea()
    guideView.visibility = View.VISIBLE
  }

  fun showErrorLayout() {
    pushInfoArea()
    errorView.visibility = View.VISIBLE
    guideView.visibility = View.INVISIBLE
  }

  fun hideErrorLayout() {
    errorView.visibility = View.GONE
  }

  private fun pushInfoArea() {
    (recyclerView.layoutParams as FrameLayout.LayoutParams).bottomMargin =
        recyclerView.context.resources.getDimension(R.dimen.footer_padding).toInt()
  }
}
