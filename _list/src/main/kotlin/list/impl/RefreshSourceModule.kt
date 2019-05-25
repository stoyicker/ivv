package list.impl

import android.content.Context
import com.nytimes.android.external.fs3.FileSystemPersisterFactory
import com.nytimes.android.external.store3.base.Fetcher
import com.nytimes.android.external.store3.base.impl.FluentStoreBuilder
import com.nytimes.android.external.store3.base.impl.StalePolicy
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.middleware.moshi.MoshiParserFactory
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import list.ParserModule
import okio.BufferedSource
import javax.inject.Singleton

@Module(includes = [ParserModule::class, ListApiModule::class])
internal class RefreshSourceModule {
  @Provides
  @Singleton
  fun store(
      context: Context,
      moshiBuilder: Moshi.Builder,
      api: ListApi) =
      FluentStoreBuilder.parsedWithKey<RefreshRequestParameters, BufferedSource, RefreshResponse>(
          Fetcher { fetch(it, api) }) {
        parsers = listOf(MoshiParserFactory.createSourceParser(
            moshiBuilder.build(),
            RefreshResponse::class.java))
        persister = FileSystemPersisterFactory.create(context.externalCacheDir!!) { it.toString() }
        stalePolicy = StalePolicy.NETWORK_BEFORE_STALE
      }

  @Provides
  @Singleton
  fun source(store: Lazy<Store<RefreshResponse, RefreshRequestParameters>>) =
      RefreshSource(store)

  private fun fetch(requestParameters: RefreshRequestParameters, api: ListApi) =
      api.topRated(requestParameters).map { it.source() }
}
