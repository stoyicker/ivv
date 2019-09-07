package list.presentation

import android.support.v7.widget.RecyclerView

/**
 * Configuration for the recycler view holding the post list.
 */
internal class ListViewConfig(private val callback: ListViewInteractionListener) {
  private val myAdapter = Adapter(callback).apply { setHasStableIds(true) }

  fun on(contentView: ContentView) =
    contentView.run {
      recyclerView.apply {
        adapter = myAdapter
        addOnScrollListener(endlessLoadListener(layoutManager!!))
        setHasFixedSize(false)
      }
      errorView.setOnClickListener { callback.onPageLoadRequested() }
    }

  /**
   * Requests a filtering command to be performed.
   * @param constraint The constraint for the filtering action.
   */
  internal fun filterView(constraint: CharSequence?) = myAdapter.filter.filter(constraint, null)

  /**
   * Provides support for the user interaction that requests loading additional items.
   *
   */
  private fun endlessLoadListener(layoutManager: RecyclerView.LayoutManager) =
      object : EndlessLoadOnScrollListener(layoutManager) {
        override fun onLoadMore() {
          callback.onPageLoadRequested()
        }
      }
}
