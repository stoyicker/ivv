package list.presentation

internal interface AdditiveLoadableContentView<in T : Any?> {
  /**
   * Called to notify the implementation that the loading layout should be shown.
   */
  fun showLoadingLayout()

  /**
   * Called to notify the implementation that the loading layout should be hidden.
   */
  fun hideLoadingLayout()

  /**
   * Called to notify the implementation that the content should be updated.
   * @param newContent A list of items that need to be displayed.
   */
  fun setContent(newContent: List<T>)

  /**
   * Called to notify the implementation that the content layout should be hidden. Optional.
   */
  fun hideContentLayout() { }

  /**
   * Called to notify the implementation that the error layout should be shown.
   */
  fun showErrorLayout()

  /**
   * Called to notify the implementation that the error layout should be hidden.
   */
  fun hideErrorLayout()
}
