package splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import tmdb.list.presentation.listActivityIntent
import javax.inject.Qualifier

@Module
internal object OpenContentRunnableModule {
  @Provides
  @JvmStatic
  @SplashActivityScope
  fun openContentRunnable(
      activity: AppCompatActivity, @LocalOpenContentRunnableModule target: Intent) =
      OpenContentRunnable(activity, target)

  @Provides
  @JvmStatic
  @SplashActivityScope
  @LocalOpenContentRunnableModule
  fun target(context: Context) = listActivityIntent(context)

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class LocalOpenContentRunnableModule
}
