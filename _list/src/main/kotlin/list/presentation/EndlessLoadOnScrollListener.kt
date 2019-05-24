package list.presentation

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager


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
    if (!loading && lastVisibleItemPosition == totalItemCount - 1) {
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
        is StaggeredGridLayoutManager -> getLastVisibleItem(
            layoutManager.findLastVisibleItemPositions(null))
        else -> throw IllegalStateException(
            """Only ${LinearLayoutManager::class.java.name} or
                    ${StaggeredGridLayoutManager::class.java.name}""")
      }

  private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
    var maxSize = 0
    for (i in lastVisibleItemPositions.indices) {
      if (i == 0) {
        maxSize = lastVisibleItemPositions[i]
      } else if (lastVisibleItemPositions[i] > maxSize) {
        maxSize = lastVisibleItemPositions[i]
      }
    }
    return maxSize
  }

  /**
   * Implement refresh logic here.
   */
  protected abstract fun onLoadMore()
}
