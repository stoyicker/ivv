package list.impl

import io.reactivex.Single

/**
 * A network request 'method'.
 */
typealias Fetchable<RequestModel, ResponseModel> = (RequestModel) -> Single<ResponseModel>
