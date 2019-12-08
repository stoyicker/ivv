package tmdb.list.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * An ImageView that allows drawable upscaling to satisfy the fitCenter scaleType.
 * @see <a href="https://github.com/triposo/barone/blob/391e533f319f010b304596d4429d03edb1e7dc96/src/com/triposo/barone/ScalingImageView.java">
 *     ScalingImageView</>
 */
internal class OutScalingImageView(context: Context, attrs: AttributeSet?)
  : AppCompatImageView(context, attrs) {
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var localHeightMeasureSpec = heightMeasureSpec
    val mDrawable = drawable
    if (mDrawable != null) {
      val mDrawableWidth = mDrawable.intrinsicWidth
      val mDrawableHeight = mDrawable.intrinsicHeight
      val actualAspect = mDrawableWidth.toFloat() / mDrawableHeight.toFloat()
      // Assuming the width is ok, calculate the height
      val actualWidth = MeasureSpec.getSize(widthMeasureSpec)
      val height = (actualWidth / actualAspect).toInt()
      localHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
    }
    super.onMeasure(widthMeasureSpec, localHeightMeasureSpec)
  }
}
