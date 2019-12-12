package splash

import android.os.Handler
import dagger.Module
import dagger.Provides

@Module
internal object HandlerModule {
  @Provides
  @JvmStatic
  @SplashActivityScope
  fun handler() = Handler()
}
