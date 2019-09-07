package list.presentation

import io.reactivex.functions.Consumer

internal class PageLoadErrorConsumer(private val view: ContentView)
  : Consumer<Throwable> {
  override fun accept(t: Throwable) =
      view.run {
        showErrorLayout()
        hideLoadingLayout()
      }
}
