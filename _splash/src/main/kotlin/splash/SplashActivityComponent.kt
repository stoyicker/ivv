package splash

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Scope

@Subcomponent(modules = [HandlerModule::class, OpenContentRunnableModule::class])
@SplashActivityScope
interface SplashActivityComponent {
  fun inject(target: SplashActivity)

  @Subcomponent.Factory
  interface Factory {
    fun create(@BindsInstance activity: AppCompatActivity): SplashActivityComponent
  }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SplashActivityScope
