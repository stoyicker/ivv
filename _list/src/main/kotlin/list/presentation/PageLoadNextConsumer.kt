package list.presentation

import io.reactivex.functions.Consumer

internal class PageLoadNextConsumer(private val view: AdditiveLoadableContentView<PresentationItem>)
  : Consumer<List<PresentationItem>> {
  override fun accept(t: List<PresentationItem>) {
    view.apply {
      addContent(t)
      hideLoadingLayout()
      hideErrorLayout()
    }
  }
}
