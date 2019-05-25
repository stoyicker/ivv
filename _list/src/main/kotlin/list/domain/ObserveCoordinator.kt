package list.domain

import hu.akarnokd.rxjava2.operators.ObservableTransformers
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.Subject
import list.impl.ListItem

internal class ObserveCoordinator(
    private val functionalityHolder: FunctionalityHolder,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler,
    private val valveSource: Subject<Boolean>,
    private val refreshCoordinator: RefreshCoordinator) {
  private var disposable: Disposable? = null

  fun run(onNext: Consumer<List<ListItem>>, onError: Consumer<Throwable>) {
    var chunkCounter = 0
    var chunks = -1
    disposable = functionalityHolder.observe
        .subscribeOn(subscribeScheduler)
        .observeOn(observeScheduler)
        .flatMapIterable {
          // Client-side pagination to avoid overwhelming the list in case the server misbehaves or
          // doesn't support pagination
          val list = it.chunked(CLIENT_PAGE_SIZE)
          chunks = list.size
          list
        }
        .compose(ObservableTransformers.valve<List<ListItem>>(valveSource))
        .doOnNext {
          if (++chunkCounter == chunks) {
            refreshCoordinator.run()
            chunkCounter = 0
          }
        }
        .doAfterNext {
          // Close the valve after we've moved one page
          valveSource.onNext(false)
        }
        .subscribe(onNext, onError)
  }

  fun abort() = disposable?.dispose()

  fun nextPage() {
    valveSource.onNext(true)
  }
}

private const val CLIENT_PAGE_SIZE = 15
