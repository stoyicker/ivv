package list.domain

import io.reactivex.Flowable
import list.impl.ListItem

internal class FunctionalityHolder {
  lateinit var observe: Flowable<List<ListItem>>
  lateinit var refresh: (Int) -> Unit
}
