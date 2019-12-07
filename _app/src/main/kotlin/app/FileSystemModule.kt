package app

import android.content.Context
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Qualifier

@Module
internal object FileSystemModule {
  @Provides
  @GlobalScope
  @JvmStatic
  fun filesystem(@LocalFileSystemModule cacheDir: File) = FileSystemFactory.create(cacheDir)

  @Provides
  @GlobalScope
  @LocalFileSystemModule
  @JvmStatic
  fun cacheDir(context: Context) = context.cacheDir

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class LocalFileSystemModule
}
