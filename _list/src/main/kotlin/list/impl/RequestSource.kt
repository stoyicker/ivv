package list.impl

import com.nytimes.android.external.store3.base.impl.Store
import io.reactivex.Single

abstract class RequestSource<in RequestModel, ResponseModel>(
    private val store: Store<ResponseModel, RequestModel>)
  : (RequestModel) -> Single<ResponseModel> {
  override fun invoke(parameters: RequestModel) = store.get(parameters)
}
