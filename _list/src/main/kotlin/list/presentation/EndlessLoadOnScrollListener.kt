package list.presentation

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * @see <a href="https://gist.githubusercontent.com/nesquena/d09dc68ff07e845cc622/raw/e2429b173f75afb408b420ad4088fed68240334c/EndlessRecyclerViewScrollListener.java">Adapted from CodePath</a>
 */
internal abstract class EndlessLoadOnScrollListener(
    private val layoutManager: RecyclerView.LayoutManager) : RecyclerView.OnScrollListener() {
  private var loading = true
  private var previousTotalItemCount = 0

  override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
    val lastVisibleItemPosition = findLastVisibleItemPosition()
    val totalItemCount = layoutManager.itemCount
    if (loading && (totalItemCount > previousTotalItemCount)) {
      loading = false
      previousTotalItemCount = totalItemCount
    }
    if (!loading && lastVisibleItemPosition == totalItemCount - ITEM_THRESHOLD) {
      loading = true
      onLoadMore()
    }
  }

  /**
   * Independent of the layout manager in use.
   */
  private fun findLastVisibleItemPosition() =
      when (layoutManager) {
        is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
        is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
        else -> throw IllegalStateException(
            """Only ${LinearLayoutManager::class.java.name} or
                    ${GridLayoutManager::class.java.name}""")
      }

  /**
   * Implement refresh logic here.
   */
  protected abstract fun onLoadMore()
}

private const val ITEM_THRESHOLD = 1
