package list

import android.content.Context
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object FileSystemModule {
  @Provides
  @Singleton
  fun filesystem(context: Context) = FileSystemFactory.create(context.externalCacheDir!!)
}
