package splash

import dagger.Subcomponent

@Subcomponent
interface RootSplashComponent {
  fun newSplashActivityComponentFactory(): SplashActivityComponent.Factory
}
