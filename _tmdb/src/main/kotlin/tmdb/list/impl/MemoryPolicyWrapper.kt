package tmdb.list.impl

import com.nytimes.android.external.store3.base.impl.FluentMemoryPolicyBuilder
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import java.util.concurrent.TimeUnit

internal class MemoryPolicyWrapper() : Lazy<MemoryPolicy> {
  override val value = FluentMemoryPolicyBuilder.build {
    expireAfterWrite = EXPIRE_AFTER_WRITE_MILLIS
    expireAfterTimeUnit = TimeUnit.MILLISECONDS
  }

  override fun isInitialized() = true
}

private const val EXPIRE_AFTER_WRITE_MILLIS = 30 * 60 * 1000L
