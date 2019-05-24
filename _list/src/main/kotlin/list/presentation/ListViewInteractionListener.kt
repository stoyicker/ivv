package list.presentation

/**
 * An interface for the view to communicate with outsiders.
 */
internal interface ListViewInteractionListener {
  /**
   * To be called when an item click happens.
   * @param item The item clicked.
   */
  fun onItemClicked(item: PresentationItem)

  /**
   * To be called when a page load is requested.
   */
  fun onPageLoadRequested()
}
