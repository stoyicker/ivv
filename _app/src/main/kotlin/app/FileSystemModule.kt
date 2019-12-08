package app

import android.content.Context
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import dagger.Module
import dagger.Provides
import java.io.File

@Module
internal object FileSystemModule {
  @Provides
  @GlobalScope
  @JvmStatic
  fun filesystem(cacheDir: File) = FileSystemFactory.create(cacheDir)

  @Provides
  @GlobalScope
  @JvmStatic
  fun cacheDir(context: Context) = context.cacheDir
}
