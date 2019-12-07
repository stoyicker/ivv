package list.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import list.impl.ListItem
import org.jorge.test.list.R

/**
 * A very simple adapter backed by a mutable list that exposes a method to add items.
 * An alternative would have been to use the databinding library, but the fact that it does not
 * support merge layouts would make diverse screen support more complicated.
 */
internal class Adapter(
    private val picasso: Picasso,
    private val callback: ListViewInteractionListener)
// TODO Make this a ListAdapter instead to get partial updates for free
// https://developer.android.com/reference/android/support/v7/recyclerview/extensions/ListAdapter
  : RecyclerView.Adapter<ViewHolder>(), Filterable {
  private var items = listOf<ListItem>()
  private var shownItems = emptyList<ListItem>()
  private lateinit var recyclerView: RecyclerView
  private val filter = RepeatableFilter()

  override fun onAttachedToRecyclerView(target: RecyclerView) {
    recyclerView = target
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false), picasso) {
    callback.onItemClicked(it)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) =
      holder.present(shownItems[position])

  override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
    if (payloads.isEmpty()) {
      onBindViewHolder(holder, position)
      return
    }
    // This is used to take the latest valid value in the given payload list
    val fold: (Bundle, String) -> Unit = { bundle, key ->
      @Suppress("UNCHECKED_CAST")
      bundle.putString(key, (payloads as List<Bundle>).fold(Bundle(), { old, new ->
        val oldTitle = old.getString(key)
        bundle.putString(key, new.getString(key) ?: oldTitle)
        new
      }).getString(key))
    }
    val combinedBundle = Bundle().apply {
      arrayOf(KEY_TITLE, KEY_THUMBNAIL).forEach {
        fold(this, it)
      }
    }
    // Now combinedBundle contains the latest version of each of the fields that can be updated
    // individually
    holder.presentPartial(combinedBundle, shownItems[position])
  }

  override fun getItemCount() = shownItems.size

  /**
   * This implementation is a bit 'meh' because of String (the type of the item id, which is
   * what we use to calculate the item hash code) being a bigger type than Long, the required one.
   */
  override fun getItemId(position: Int) = shownItems[position].hashCode().toLong()

  /**
   * Requests a list of items to be set in this adapter. This call re-applies the current
   * filter, which means items which do not meet the current filter will not be shown.
   * @param newItems The items.
   */
  internal fun setItems(newItems: List<ListItem>) {
    items = newItems
    filter.refresh()
  }

  override fun getFilter() = filter

  /**
   * A filter that keeps track of its last query for repetition.
   */
  internal inner class RepeatableFilter : Filter() {
    private var currentQuery: CharSequence = ""
    private lateinit var diff: DiffUtil.DiffResult

    override fun performFiltering(constraint: CharSequence?): FilterResults? {
      currentQuery = constraint?.trim() ?: ""
      val filteredItems = if (currentQuery.isBlank()) {
        items
      } else {
        items.filter { it.name?.contains(currentQuery, true) ?: false }
      }
      diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize() = shownItems.size

        override fun getNewListSize() = filteredItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            shownItems[oldItemPosition].id.let { oldId ->
              filteredItems[newItemPosition].id.let { newId ->
                oldId == newId
              }
            }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            shownItems[oldItemPosition].let {
              it.id == filteredItems[newItemPosition].id &&
                  it.name == filteredItems[newItemPosition].name
            }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Bundle {
          shownItems[oldItemPosition].let { old ->
            filteredItems[newItemPosition].let { new ->
              return Bundle().apply {
                putString(KEY_TITLE, new.name.takeIf {
                  it?.contentEquals(old.name ?: it) ?: old.name != null
                })
                putString(KEY_THUMBNAIL, new.posterPath.takeIf {
                  it?.contentEquals(old.posterPath ?: it) ?: old.posterPath != null
                })
              }
            }
          }
        }
      })
      return FilterResults().also {
        it.values = filteredItems
        it.count = filteredItems.size
      }
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
      val shouldScrollToTop = shownItems != results!!.values
      @Suppress("UNCHECKED_CAST")
      shownItems = results?.values as List<ListItem>? ?: items
      diff.dispatchUpdatesTo(this@Adapter)
      if (shouldScrollToTop) {
        recyclerView.scrollToPosition(0)
      }
    }

    /**
     * Queues a new filtering action with the last query.
     */
    fun refresh() = filter(currentQuery)
  }
}

internal const val KEY_TITLE = "KEY_TITLE"
internal const val KEY_THUMBNAIL = "KEY_THUMBNAIL"
