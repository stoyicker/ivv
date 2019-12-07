package list.impl

import com.nytimes.android.external.store3.base.Parser
import com.nytimes.android.external.store3.base.Persister
import com.nytimes.android.external.store3.base.impl.FluentStoreBuilder
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.base.impl.StalePolicy
import com.nytimes.android.external.store3.base.impl.Store
import okio.BufferedSource

internal class StoreWrapper(
    fetcher: ListFetcher,
    parserList: MutableList<Parser<BufferedSource, RefreshResponse>>,
    recordPersister: Persister<BufferedSource, Int>,
    memPolicy: MemoryPolicy,
    stPolicy: StalePolicy) : Lazy<Store<RefreshResponse, Int>> {
  override val value = FluentStoreBuilder.parsedWithKey<Int, BufferedSource, RefreshResponse>(
      fetcher) {
    parsers = parserList
    persister = recordPersister
    memoryPolicy = memPolicy
    stalePolicy = stPolicy
  }

  override fun isInitialized() = true
}
