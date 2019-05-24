package list.presentation

import io.reactivex.functions.Consumer

internal class PageLoadErrorConsumer(private val view: AdditiveLoadableContentView<*>)
  : Consumer<Throwable> {
  override fun accept(t: Throwable) {
    view.apply {
      showErrorLayout()
      hideLoadingLayout()
      hideContentLayout()
    }
  }
}
