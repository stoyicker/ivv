package list.impl

import com.nytimes.android.external.store3.base.impl.Store

abstract class RequestSource<in RequestModel, ResponseModel>(
    private val store: Store<ResponseModel, RequestModel>)
  : Gettable<RequestModel, ResponseModel> {
  override fun invoke(parameters: RequestModel) = store.get(parameters)
}
