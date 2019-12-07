package list.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jorge.test.list.R

/**
 * A recycler view that automatically attaches to itself a GridLayoutManager and performs auto-fit
 * with its elements.
 * @param context The context
 * @param attrs The attributes
 * @see <a href="http://blog.sqisland.com/2014/12/recyclerview-autofit-grid.html">
 *     Square Island: RecyclerView: Autofit grid</a>
 */
internal class AutoFitGridRecyclerView(context: Context, attrs: AttributeSet?)
  : RecyclerView(context, attrs) {
  @Px
  private val columnWidth: Int

  init {
    @Px val defaultColumnWidth = context.resources.getDimension(R.dimen.default_column_width).toInt()
    if (attrs != null) {
      val attrsArray = intArrayOf(android.R.attr.columnWidth)
      val typedArray = context.obtainStyledAttributes(attrs, attrsArray)
      columnWidth = typedArray.getDimensionPixelSize(0, defaultColumnWidth)
      typedArray.recycle()
    } else {
      columnWidth = defaultColumnWidth
    }
    layoutManager = object : GridLayoutManager(context, 1) {
      override fun supportsPredictiveItemAnimations() = false
    }
  }

  override fun onMeasure(widthSpec: Int, heightSpec: Int) {
    super.onMeasure(widthSpec, heightSpec)
    if (!isInEditMode && columnWidth > 0) {
      val spanCount = (measuredWidth / columnWidth).coerceAtLeast(1)
      (layoutManager as GridLayoutManager).spanCount = spanCount
    }
  }
}
