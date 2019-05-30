package list.impl

import io.reactivex.Single

/**
 * A network request 'method'.
 */
typealias Gettable<RequestModel, ResponseModel> = (RequestModel) -> Single<ResponseModel>
