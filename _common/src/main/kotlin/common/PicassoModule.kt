package common

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PicassoModule {
  @Provides
  @Singleton
  fun picasso() = Picasso.get()
}
