package list.impl

import com.nytimes.android.external.fs3.filesystem.FileSystem
import com.nytimes.android.external.store3.base.Parser
import com.nytimes.android.external.store3.base.Persister
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.base.impl.StalePolicy
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.middleware.moshi.MoshiParserFactory
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okio.BufferedSource
import javax.inject.Qualifier

@Module(includes = [ListApiModule::class])
internal object RefreshSourceModule {
  @Provides
  @InitializationContentProviderScope
  @JvmStatic
  fun source(@LocalRefreshSourceModule store: Lazy<Store<RefreshResponse, Int>>) =
      RefreshSource(store)

  @Provides
  @InitializationContentProviderScope
  @LocalRefreshSourceModule
  @JvmStatic
  fun fetcher(api: ListApi) = ListFetcher(api)

  @Provides
  @InitializationContentProviderScope
  @LocalRefreshSourceModule
  @JvmStatic
  fun parsers(moshiBuilder: Moshi.Builder): List<Parser<BufferedSource, RefreshResponse>> =
      listOf(MoshiParserFactory.createSourceParser<RefreshResponse>(
          moshiBuilder.build(),
          RefreshResponse::class.java))

  @Provides
  @InitializationContentProviderScope
  @LocalRefreshSourceModule
  @JvmStatic
  fun filesystemRecordPersister(fileSystem: FileSystem) = PersisterWrapper(fileSystem).value

  @Provides
  @InitializationContentProviderScope
  @LocalRefreshSourceModule
  @JvmStatic
  fun memPolicy() = MemoryPolicyWrapper().value

  @Provides
  @InitializationContentProviderScope
  @LocalRefreshSourceModule
  @JvmStatic
  fun stalePolicy() = StalePolicy.NETWORK_BEFORE_STALE

  @Provides
  @InitializationContentProviderScope
  @LocalRefreshSourceModule
  @JvmStatic
  fun store(
      @LocalRefreshSourceModule
      fetcher: ListFetcher,
      @LocalRefreshSourceModule
      parserList: MutableList<Parser<BufferedSource, RefreshResponse>>,
      @LocalRefreshSourceModule
      recordPersister: Persister<BufferedSource, Int>,
      @LocalRefreshSourceModule
      memPolicy: MemoryPolicy,
      @LocalRefreshSourceModule
      stPolicy: StalePolicy) =
      StoreWrapper(fetcher, parserList, recordPersister, memPolicy, stPolicy).value

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class LocalRefreshSourceModule
}
