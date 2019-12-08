package app

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Qualifier

@Module
internal object PicassoModule {
  @Provides
  @GlobalScope
  @JvmStatic
  fun picasso(context: Context, @LocalPicassoModule okHttp3Downloader: OkHttp3Downloader): Picasso {
    Picasso.setSingletonInstance(Picasso.Builder(context)
        .downloader(okHttp3Downloader)
        .build())
    return Picasso.get()
  }

  @Provides
  @GlobalScope
  @JvmStatic
  @LocalPicassoModule
  fun okHttp3Downloader(okhttpClientBuilder: OkHttpClient.Builder) =
      OkHttp3Downloader(okhttpClientBuilder.build())

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class LocalPicassoModule
}
