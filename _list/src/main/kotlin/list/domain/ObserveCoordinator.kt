package list.domain

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.PublishProcessor
import list.presentation.PresentationItem

internal class ObserveCoordinator(
    private val functionalityHolder: FunctionalityHolder,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler,
    private val valveSource: PublishProcessor<Boolean>) {
  private var disposable: Disposable? = null

  fun run(onNext: Consumer<List<PresentationItem>>, onError: Consumer<Throwable>) {
    // TODO Use a relay here instead, this is never going to complete
    disposable = functionalityHolder.observe
        .doOnNext {
          // Whenever the source of truth refreshes, we need to
          // (1) drop non-emitted pages from the previous source -> Ditch this subscription
          // (2) notify that the previous items are no longer valid -> Emit an empty list
          // (will trigger a clear)
          onNext.accept(emptyList())
          disposable?.dispose()
          run(onNext, onError)
        }
        .subscribeOn(subscribeScheduler)
        .observeOn(observeScheduler)
        .flatMapIterable {
          // Client-side paging to avoid overwhelming the list
          it.chunked(PAGE_SIZE)
        }
        .doAfterNext {
          // Close the valve after we've moved one page
          valveSource.onNext(false)
        }
        .subscribe(onNext, onError)
  }

  fun abort() = disposable?.dispose()

  fun nextPage() = valveSource.onNext(true)
}

private const val PAGE_SIZE = 50
