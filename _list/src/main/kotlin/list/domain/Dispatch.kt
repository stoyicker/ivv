package list.domain

/**
 * Describes the requirement for the action of refreshing a refresh of the source of truth of the
 * contents in the list.
 * I wish this could be a typealias, but that won't work with Dagger.
 */
internal interface Refresh : (Int) -> Unit
