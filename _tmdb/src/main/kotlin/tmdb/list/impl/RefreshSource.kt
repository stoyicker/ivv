package tmdb.list.impl

import com.nytimes.android.external.store3.base.impl.Store
import dagger.Lazy

internal class RefreshSource(storeAccessor: Lazy<Store<RefreshResponse, Int>>)
  : RequestSource<Int, RefreshResponse>(storeAccessor.get())
