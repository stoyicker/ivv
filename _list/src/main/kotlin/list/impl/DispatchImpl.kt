package list.impl

import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import list.domain.Refresh

internal class RefreshImpl(
    private val refreshSource: RefreshSource,
    private val scheduler: Scheduler,
    private val truthSource: BehaviorSubject<ItemPage>) : Refresh {
  override fun invoke(page: Int) {
    refreshSource(page)
        .retry()
        .subscribeOn(scheduler)
        .subscribe(object : SingleObserver<RefreshResponse> {
          /**
           * If we asked for a new page, add it to the truth. Otherwise, reset the truth to what we
           * just got.
           */
          override fun onSuccess(t: RefreshResponse) {
            val resetTruth = truthSource.value?.page ?: Int.MAX_VALUE >= page
            if (resetTruth) {
              truthSource.onNext(ItemPage(0, emptyList()))
            }
            truthSource.onNext(ItemPage(page, t.results))
          }

          override fun onSubscribe(d: Disposable) = Unit

          override fun onError(e: Throwable) = Unit
        })
  }
}
