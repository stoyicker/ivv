package list.domain

import io.reactivex.Observable
import list.impl.ListItem

/**
 * Describes the requirement for the source of truth for the content of the list.
 */
internal typealias Observe = Observable<List<ListItem>>
