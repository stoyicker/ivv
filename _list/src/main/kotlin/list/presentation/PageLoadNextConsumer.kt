package list.presentation

import io.reactivex.functions.Consumer
import list.impl.ListItem

class PageLoadNextConsumer(private val view: ContentView)
  : Consumer<List<ListItem>> {
  override fun accept(t: List<ListItem>) =
      view.run {
        setContent(t)
        hideLoadingLayout()
        hideErrorLayout()
      }
}
