package tmdb.list.presentation

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import org.jorge.test.tmdb.R
import tmdb.list.impl.ListItem

class ContentView(
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
    (recyclerView.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin =
        recyclerView.context.resources.getDimension(R.dimen.footer_padding).toInt()
  }
}
