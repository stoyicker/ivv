package splash.presentation

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import list.presentation.listActivityIntent
import testaccessors.RequiresAccessor

/**
 * A simple activity that acts as a splash screen.
 * Note how, instead of using the content view to set the splash, we just set the splash as
 * background in the theme. This allows it to be shown without having to wait for the content view
 * to be drawn.
 */
internal class SplashActivity : AppCompatActivity() {
  private lateinit var handler: Handler

  override fun onResume() {
    super.onResume()
    scheduleContentOpening()
  }

  /**
   * Schedules the app content to be shown.
   */
  private fun scheduleContentOpening() {
    handler = Handler().apply {
      postDelayed({ openContent() }, SHOW_TIME_MILLIS)
    }
  }

  /**
   * Closes the splash and introduces the actual content of the app.
   */
  private fun openContent() {
    startActivity(listActivityIntent(this).apply {
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    })
    supportFinishAfterTransition()
  }

  override fun onPause() {
    handler.removeCallbacksAndMessages(null)
    super.onPause()
  }
}

@RequiresAccessor(requires = [RequiresAccessor.AccessorType.TYPE_GETTER])
private const val SHOW_TIME_MILLIS = 1000L
