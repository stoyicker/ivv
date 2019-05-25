package list.presentation

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.item.view.thumbnail
import kotlinx.android.synthetic.main.item.view.title_view
import list.impl.ListItem

/**
 * Very simple viewholder that sets text and click event handling.
 * @param itemView The view to dump the held data.
 * @param onItemClicked What to run when a click happens.
 */
internal class ViewHolder internal constructor(
    itemView: View,
    private val onItemClicked: (ListItem) -> Unit)
  : RecyclerView.ViewHolder(itemView), Target {

  /**
   * Draw an item.
   * @title The item to draw.
   */
  internal fun render(item: ListItem) {
    item.name?.let { setTitle(it) }
    item.posterPath?.let { setThumbnail(it) }
    itemView.setOnClickListener { onItemClicked(item) }
  }

  /**
   * Performs partial re-drawing of an item.
   * @param bundle The updates that need to be drawn.
   * @param item The item these updates correspond to.
   */
  internal fun renderPartial(bundle: Bundle, item: ListItem) {
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

  override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) = Unit

  /**
   * Updates the layout according to the changes required by a new thumbnail link.
   * @param thumbnailLink The new thumbnail link, or <code>null</code> if none is applicable.
   */
  private fun setThumbnail(thumbnailLink: String?) {
    itemView.thumbnail.let {
      if (thumbnailLink != null) {
        // TODO have the field be the whole string instead by combining the results of the call to
        //  getting the list and the config endpoint. Bit too much for this scope
        val fullLink = "https://image.tmdb.org/t/p/w780$thumbnailLink"
        Picasso.get().load(fullLink).into(this)
      } else {
        it.visibility = View.GONE
        it.setImageDrawable(null)
      }
    }
  }
}