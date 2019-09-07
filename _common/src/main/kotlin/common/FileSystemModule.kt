package common

import android.content.Context
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class FileSystemModule {
  @Provides
  @Singleton
  fun filesystem(@Local cacheDir: File) = FileSystemFactory.create(cacheDir)

  @Provides
  @Singleton
  @Local
  internal fun cacheDir(context: Context) = context.cacheDir

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class Local
}
