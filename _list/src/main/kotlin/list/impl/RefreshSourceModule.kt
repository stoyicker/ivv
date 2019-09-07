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
import common.FileSystemModule
import common.ParserModule
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okio.BufferedSource
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ParserModule::class, ListApiModule::class, FileSystemModule::class])
internal class RefreshSourceModule {
  @Provides
  @Singleton
  @Local
  fun fetcher(api: ListApi) = ListFetcher(api)

  @Provides
  @Singleton
  @Local
  fun parsers(moshiBuilder: Moshi.Builder): List<Parser<BufferedSource, RefreshResponse>> =
      listOf(MoshiParserFactory.createSourceParser<RefreshResponse>(
          moshiBuilder.build(),
          RefreshResponse::class.java))

  @Provides
  @Singleton
  @Local
  fun filesystemRecordPersister(fileSystem: FileSystem)
      : Persister<BufferedSource, Int> = FileSystemRecordPersister.create(
      fileSystem,
      { it.toString() },
      1,
      TimeUnit.HOURS)

  @Provides
  @Singleton
  @Local
  fun memPolicy() = FluentMemoryPolicyBuilder.build {
    expireAfterWrite = 30
    expireAfterTimeUnit = TimeUnit.MINUTES
  }

  @Provides
  @Singleton
  @Local
  fun stalePolicy() = StalePolicy.NETWORK_BEFORE_STALE

  @Provides
  @Singleton
  @Local
  fun store(
      @Local
      fetcher: ListFetcher,
      @Local
      parserList: MutableList<Parser<BufferedSource, RefreshResponse>>,
      @Local
      recordPersister: Persister<BufferedSource, Int>,
      @Local
      memPolicy: MemoryPolicy,
      @Local
      stPolicy: StalePolicy) =
      FluentStoreBuilder.parsedWithKey<Int, BufferedSource, RefreshResponse>(
          fetcher) {
        parsers = parserList
        persister = recordPersister
        memoryPolicy = memPolicy
        stalePolicy = stPolicy
      }

  @Provides
  @Singleton
  fun source(@Local store: Lazy<Store<RefreshResponse, Int>>) = RefreshSource(store)

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class Local
}
