package list.domain

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import list.impl.ListItem

internal class ObserveCoordinator(
    private val functionalityHolder: FunctionalityHolder,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler) : IObserveCoordinator {
  private var disposable: Disposable? = null

  override fun run(onNext: Consumer<List<ListItem>>, onError: Consumer<Throwable>) {
    disposable = functionalityHolder.observe
        .subscribeOn(subscribeScheduler)
        .observeOn(observeScheduler)
        .subscribe(onNext, onError)
  }

  override fun abort() {
    disposable?.dispose()
  }
}

// For testing
internal interface IObserveCoordinator {
  fun run(onNext: Consumer<List<ListItem>>, onError: Consumer<Throwable>)

  fun abort()
}
