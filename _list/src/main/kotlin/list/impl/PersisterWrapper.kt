package list.impl

import com.nytimes.android.external.fs3.FileSystemRecordPersister
import com.nytimes.android.external.fs3.filesystem.FileSystem
import com.nytimes.android.external.store3.base.Persister
import okio.BufferedSource
import java.util.concurrent.TimeUnit

internal class PersisterWrapper(fileSystem: FileSystem) : Lazy<Persister<BufferedSource, Int>> {
  override val value: Persister<BufferedSource, Int> = FileSystemRecordPersister.create(
      fileSystem,
      { it.toString() },
      EXPIRE_AFTER_WRITE_MILLIS,
      TimeUnit.MILLISECONDS)

  override fun isInitialized() = true
}

private const val EXPIRE_AFTER_WRITE_MILLIS = 60 * 60 * 1000L
