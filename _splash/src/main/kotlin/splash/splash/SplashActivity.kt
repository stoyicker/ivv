package splash

import android.app.Application
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import app.ComponentProvider
import javax.inject.Inject

/**
 * A simple activity that acts as a splash screen.
 * Note how, instead of using the content view to set the splash, we just set the splash as
 * background in the theme. This allows it to be shown without having to wait for the content view
 * to be drawn.
 */
class SplashActivity : AppCompatActivity() {
  @Inject
  lateinit var handler: Handler
  @Inject
  internal lateinit var openContentRunnable: OpenContentRunnable

  override fun onResume() {
    super.onResume()
    componentF(application, this).inject(this)
    scheduleContentOpening()
  }

  /**
   * Schedules the app content to be shown.
   */
  private fun scheduleContentOpening() =
    handler.postDelayed(openContentRunnable, SHOW_TIME_MILLIS)

  override fun onPause() {
    handler.removeCallbacksAndMessages(null)
    super.onPause()
  }
}

private const val SHOW_TIME_MILLIS = 1000L

private var componentF = { application: Application, activity: AppCompatActivity ->
  @Suppress("UNCHECKED_CAST")
  (application as ComponentProvider).moduleRootComponent(RootSplashComponent::class)
      .newSplashActivityComponentFactory()
      .create(activity)
}
