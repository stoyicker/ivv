package list.presentation

import list.impl.ListItem

/**
 * An interface for the view to communicate with outsiders.
 */
interface ListViewInteractionListener {
  /**
   * To be called when an item click happens.
   * @param item The item clicked.
   */
  fun onItemClicked(item: ListItem)

  /**
   * To be called when a page load is requested.
   */
  fun onPageLoadRequested()
}
