package list.domain

import io.reactivex.Observable
import list.impl.ListItem

internal object FunctionalityHolder {
  lateinit var observe: Observable<List<ListItem>>
  lateinit var refresh: (Int) -> Unit
}
