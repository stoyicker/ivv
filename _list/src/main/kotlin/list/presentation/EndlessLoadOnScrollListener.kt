package list.presentation

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * @see <a href="https://gist.githubusercontent.com/nesquena/d09dc68ff07e845cc622/raw/e2429b173f75afb408b420ad4088fed68240334c/EndlessRecyclerViewScrollListener.java">Adapted from CodePath</a>
 */
internal abstract class EndlessLoadOnScrollListener(
    private val layoutManager: RecyclerView.LayoutManager) : RecyclerView.OnScrollListener() {
  override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
    if ((layoutManager as GridLayoutManager).findLastVisibleItemPosition() ==
        layoutManager.itemCount - 1 && dy > 0) {
      onLoadMore()
    }
  }

  /**
   * Implement refresh logic here.
   */
  protected abstract fun onLoadMore()
}
