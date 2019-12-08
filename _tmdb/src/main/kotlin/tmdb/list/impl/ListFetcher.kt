package tmdb.list.impl

import com.nytimes.android.external.store3.base.Fetcher
import okio.BufferedSource

internal class ListFetcher(private val api: ListApi) : Fetcher<BufferedSource, Int> {
  override fun fetch(key: Int) = api.tvPopular(key.coerceAtMost(LAST_PAGE)).map { it.source() }
}

private const val LAST_PAGE = 500
