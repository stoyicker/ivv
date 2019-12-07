package splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

internal class OpenContentRunnable(
    private val activity: AppCompatActivity,
    private val target: Intent) : Runnable {
  override fun run() = activity.run {
    startActivity(target.apply {
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    })
    supportFinishAfterTransition()
  }
}
