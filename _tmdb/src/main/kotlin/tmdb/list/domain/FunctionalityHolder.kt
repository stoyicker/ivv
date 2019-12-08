package tmdb.list.domain

import io.reactivex.Flowable
import tmdb.list.impl.ListItem

class FunctionalityHolder {
  lateinit var observe: Flowable<List<ListItem>>
  lateinit var refresh: (Int) -> Unit
}
