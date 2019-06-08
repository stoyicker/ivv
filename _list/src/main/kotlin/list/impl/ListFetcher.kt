package list.impl

import com.nytimes.android.external.store3.base.Fetcher
import okio.BufferedSource

internal class ListFetcher(private val api: ListApi)
  : Fetcher<BufferedSource, RefreshRequestParameters> {
  override fun fetch(key: RefreshRequestParameters) = api.topRated(key).map { it.source() }
}
