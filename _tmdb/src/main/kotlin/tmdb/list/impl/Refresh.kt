package tmdb.list.impl

import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

internal class Refresh(
    private val refreshSource: RefreshSource,
    private val scheduler: Scheduler,
    private val truthSource: BehaviorSubject<List<ListItem>>) : (Int) -> Unit {
  override fun invoke(page: Int) {
    refreshSource(page)
        .retry()
        .subscribeOn(scheduler)
        .subscribe(object : SingleObserver<RefreshResponse> {
          override fun onSuccess(t: RefreshResponse) {
            val previousTruth = truthSource.value
            if (previousTruth != null) {
              val updated: Set<ListItem> = t.results.intersect(previousTruth)
              truthSource.onNext(updated.union(previousTruth).union(t.results).toList())
            } else {
              truthSource.onNext(t.results)
            }
          }

          override fun onSubscribe(d: Disposable) = Unit

          override fun onError(e: Throwable) = Unit
        })
  }
}
