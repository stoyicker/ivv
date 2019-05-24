package list.domain

import io.reactivex.Flowable
import list.presentation.PresentationItem

/**
 * Describes the requirement for the source of truth for the content of the list.
 */
internal typealias Observe = Flowable<List<PresentationItem>>
