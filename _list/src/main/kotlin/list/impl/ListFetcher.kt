package list.impl

import com.nytimes.android.external.store3.base.Fetcher
import okio.BufferedSource

internal class ListFetcher(private val api: ListApi) : Fetcher<BufferedSource, Int> {
  override fun fetch(key: Int) = api.topRated(key).map { it.source() }
}
