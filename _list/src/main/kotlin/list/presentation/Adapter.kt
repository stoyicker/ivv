package list.presentation

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.item.view.thumbnail
import kotlinx.android.synthetic.main.item.view.title_view
import org.jorge.test.list.R

/**
 * A very simple adapter backed by a mutable list that exposes a method to add items.
 * An alternative would have been to use the databinding library, but the fact that it does not
 * support merge layouts would make diverse screen support more complicated.
 */
internal class Adapter(private val callback: ListViewInteractionListener)
// TODO Make this a ListAdapter instead to get partial updates for free and reduce verbosity
// https://developer.android.com/reference/android/support/v7/recyclerview/extensions/ListAdapter
  : RecyclerView.Adapter<Adapter.ViewHolder>(), Filterable {
  private var items = listOf<PresentationItem>()
  private var shownItems = emptyList<PresentationItem>()
  private lateinit var recyclerView: RecyclerView
  private val filter = RepeatableFilter()

  override fun onAttachedToRecyclerView(target: RecyclerView) {
    recyclerView = target
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(
      R.layout.item, parent, false)) { callback.onItemClicked(it) }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) =
      holder.render(shownItems[position])

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
    val combinedBundle = Bundle().also { bundle ->
      arrayOf(KEY_TITLE, KEY_THUMBNAIL).forEach {
        fold(bundle, it)
      }
    }
    // Now combinedBundle contains the latest version of each of the fields that can be updated
    // individually
    holder.renderPartial(combinedBundle, shownItems[position])
  }

  override fun getItemCount() = shownItems.size

  /**
   * This implementation is a bit 'meh' because of String (the type of the item id, which is
   * what we use to calculate the item hash code) being a bigger type than Long, the required one.
   */
  override fun getItemId(position: Int) = shownItems[position].hashCode().toLong()

  /**
   * Requests a list of items to be added to this adapter. This call re-applies the current
   * filter, which means some of the items passed in to be added will not be shown they don't meet
   * the current filter.
   * @param toAdd The items to add.
   */
  internal fun addItems(toAdd: List<PresentationItem>) {
    items = if (toAdd.isNotEmpty()) {
      items.plus(toAdd).distinct()
    } else {
      // If the list is empty the source of truth is outdated. Drop our current data and
      // eventually we'll get fresh one
      toAdd
    }
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
        items.filter { it.title.contains(currentQuery, true) }
      }
      diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize() = shownItems.size

        override fun getNewListSize() = filteredItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            shownItems[oldItemPosition].let { (oldId) ->
              filteredItems[newItemPosition].let { (newId) ->
                oldId.contentEquals(newId)
              }
            }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            shownItems[oldItemPosition].let {
              it.id == shownItems[newItemPosition].id &&
                  it.title == shownItems[newItemPosition].title
            }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) =
            shownItems[oldItemPosition].let { (_, oldTitle, oldThumbnail) ->
              filteredItems[newItemPosition].let { (_, newTitle, newThumbnail) ->
                Bundle().apply {
                  putString(KEY_TITLE, newTitle.takeIf {
                    !it.contentEquals(oldTitle)
                  })
                  putString(KEY_THUMBNAIL, newThumbnail.takeIf {
                    it != oldThumbnail
                  })
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
      @Suppress("UNCHECKED_CAST")
      shownItems = results?.values as List<PresentationItem>? ?: items
      diff.dispatchUpdatesTo(this@Adapter)
    }

    /**
     * Queues a new filtering action with the last query.
     */
    fun refresh() = filter(currentQuery)
  }

  /**
   * Very simple viewholder that sets text and click event handling.
   * @param itemView The view to dump the held data.
   * @param onItemClicked What to run when a click happens.
   */
  internal class ViewHolder internal constructor(
      itemView: View,
      private val onItemClicked: (PresentationItem) -> Unit)
    : RecyclerView.ViewHolder(itemView), Target {

    /**
     * Draw an item.
     * @title The item to draw.
     */
    internal fun render(item: PresentationItem) {
      setTitle(item.title)
      setThumbnail(item.thumbnailLink)
      itemView.setOnClickListener { onItemClicked(item) }
    }

    /**
     * Performs partial re-drawing of an item.
     * @param bundle The updates that need to be drawn.
     * @param item The item these updates correspond to.
     */
    internal fun renderPartial(bundle: Bundle, item: PresentationItem) {
      bundle.getString(KEY_TITLE)?.let { setTitle(it) }
      setThumbnail(bundle.getString(KEY_THUMBNAIL))
      itemView.setOnClickListener { onItemClicked(item) }
    }

    /**
     * Updates the layout according to the changes required by a new title.
     * @param title The new title.
     */
    private fun setTitle(title: String) {
      itemView.title_view.text = title
      itemView.thumbnail.contentDescription = title
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
      itemView.thumbnail.visibility = View.GONE
      itemView.thumbnail.setImageDrawable(null)
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
      itemView.thumbnail.setImageBitmap(bitmap)
      itemView.thumbnail.visibility = View.VISIBLE
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

    /**
     * Updates the layout according to the changes required by a new thumbnail link.
     * @param thumbnailLink The new thumbnail link, or <code>null</code> if none is applicable.
     */
    private fun setThumbnail(thumbnailLink: String?) {
      itemView.thumbnail.let {
        if (thumbnailLink != null) {
          Picasso.get().load(thumbnailLink).into(this)
        } else {
          it.visibility = View.GONE
          it.setImageDrawable(null)
        }
      }
    }
  }
}

private const val KEY_TITLE = "KEY_TITLE"
private const val KEY_THUMBNAIL = "KEY_THUMBNAIL"
