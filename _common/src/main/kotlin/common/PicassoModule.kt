package common

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PicassoModule {
  @Provides
  @Singleton
  @JvmStatic
  fun picasso(context: Context): Picasso {
    Picasso.setSingletonInstance(Picasso.Builder(context)
        .downloader(OkHttp3Downloader(context, Long.MAX_VALUE))
        .build())
    return Picasso.get()
  }
}
