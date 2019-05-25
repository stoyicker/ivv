package list.presentation

import io.reactivex.functions.Consumer
import list.impl.ListItem

internal class PageLoadNextConsumer(private val view: AdditiveLoadableContentView<ListItem>)
  : Consumer<List<ListItem>> {
  override fun accept(t: List<ListItem>) {
    view.apply {
      addContent(t)
      hideLoadingLayout()
      hideErrorLayout()
    }
  }
}
