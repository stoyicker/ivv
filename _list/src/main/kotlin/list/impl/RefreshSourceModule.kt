package list.impl

import com.nytimes.android.external.fs3.FileSystemRecordPersister
import com.nytimes.android.external.fs3.filesystem.FileSystem
import com.nytimes.android.external.store3.base.Parser
import com.nytimes.android.external.store3.base.Persister
import com.nytimes.android.external.store3.base.impl.FluentMemoryPolicyBuilder
import com.nytimes.android.external.store3.base.impl.FluentStoreBuilder
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.base.impl.StalePolicy
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.middleware.moshi.MoshiParserFactory
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import list.FileSystemModule
import list.ParserModule
import okio.BufferedSource
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ParserModule::class, ListApiModule::class, FileSystemModule::class])
internal class RefreshSourceModule {
  @Provides
  @Singleton
  fun fetcher(api: ListApi) = ListFetcher(api)

  @Provides
  @Singleton
  fun parsers(moshiBuilder: Moshi.Builder): List<Parser<BufferedSource, RefreshResponse>> = mutableListOf(MoshiParserFactory
      .createSourceParser<RefreshResponse>(
          moshiBuilder.build(),
          RefreshResponse::class.java))

  @Provides
  @Singleton
  fun filesystemRecordPersister(fileSystem: FileSystem)
      : Persister<BufferedSource, RefreshRequestParameters> = FileSystemRecordPersister.create(
      fileSystem,
      { it.toString() },
      1,
      TimeUnit.HOURS)

  @Provides
  @Singleton
  fun memPolicy() = FluentMemoryPolicyBuilder.build {
    expireAfterWrite = 30
    expireAfterTimeUnit = TimeUnit.MINUTES
  }

  @Provides
  @Singleton
  fun stalePolicy() = StalePolicy.NETWORK_BEFORE_STALE

  @Provides
  @Singleton
  fun store(
      fetcher: ListFetcher,
      parserList: MutableList<Parser<BufferedSource, RefreshResponse>>,
      recordPersister: Persister<BufferedSource, RefreshRequestParameters>,
      memPolicy: MemoryPolicy,
      stPolicy: StalePolicy) =
      FluentStoreBuilder.parsedWithKey<RefreshRequestParameters, BufferedSource, RefreshResponse>(
          fetcher) {
        parsers = parserList
        persister = recordPersister
        memoryPolicy = memPolicy
        stalePolicy = stPolicy
      }

  @Provides
  @Singleton
  fun source(store: Lazy<Store<RefreshResponse, RefreshRequestParameters>>) =
      RefreshSource(store)
}
