package list.impl

import com.nytimes.android.external.store3.base.impl.Store
import dagger.Lazy

internal class RefreshSource (
    storeAccessor: Lazy<Store<RefreshResponse, RefreshRequestParameters>>)
  : RequestSource<RefreshRequestParameters, RefreshResponse>(storeAccessor.get())
