package list.impl

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import io.reactivex.Observable
import common.FileSystemModule
import common.NetworkClientModule
import common.NetworkModule
import common.ParserModule
import common.SchedulerModule
import list.domain.FunctionalityHolder
import list.domain.FunctionalityHolderModule
import list.domain.Refresh
import javax.inject.Inject

/**
 * Allows this layer to hook up to the application lifecycle and, most importantly, access a
 * context. This initialization pattern is used by libraries such as Firebase and Picasso.
 *
 * @see <a href="https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html">
 * The Firebase Blog: How does Firebase initialize on Android</a>
 */
internal class InitializationContentProvider : ContentProvider() {
  // DI root for this layer in this module. See dependencies.gradle for a more detailed explanation
  private val componentF: (Context) -> InitializationContentProviderComponent = {
    DaggerInitializationContentProviderComponent.builder()
        .context(it)
        .functionalityHolderModule(FunctionalityHolderModule)
        .observeModule(ObserveModule)
        .refreshModule(RefreshModule)
        .apiKeyInterceptorModule(ApiKeyInterceptorModule)
        .listApiModule(ListApiModule)
        .networkClientModule(NetworkClientModule)
        .networkModule(NetworkModule)
        .filesystemModule(FileSystemModule)
        .parserModule(ParserModule)
        .schedulerModule(SchedulerModule)
        .build()
  }
  @Inject
  lateinit var functionalityHolder: FunctionalityHolder
  @Inject
  lateinit var observeImpl: Observable<List<ListItem>>
  @Inject
  lateinit var refreshImpl: Refresh

  /**
   * This gets called on application creation and provides functionality implementations into the
   * holder, therefore allowing the domain layer to trigger functions without knowing their
   * implementations.
   */
  override fun onCreate() = true.also {
    componentF(context!!).inject(this)
    functionalityHolder.apply {
      observe = observeImpl
      refresh = refreshImpl
    }
  }

  override fun insert(uri: Uri, values: ContentValues?) = throw UnsupportedOperationException()

  override fun query(
      uri: Uri,
      projection: Array<String>?,
      selection: String?,
      selectionArgs: Array<String>?,
      sortOrder: String?) = throw UnsupportedOperationException()

  override fun update(
      uri: Uri,
      values: ContentValues?,
      selection: String?,
      selectionArgs: Array<String>?) = throw UnsupportedOperationException()

  override fun getType(uri: Uri) = throw UnsupportedOperationException()

  override fun delete(
      uri: Uri,
      selection: String?,
      selectionArgs: Array<String>?) = throw UnsupportedOperationException()
}
