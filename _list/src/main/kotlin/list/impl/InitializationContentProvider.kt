package list.impl

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri
import app.ComponentProvider
import io.reactivex.Flowable
import list.RootListComponent
import list.domain.FunctionalityHolder
import javax.inject.Inject

/**
 * Allows this layer to hook up to the application lifecycle and, most importantly, access a
 * context. This initialization pattern is used by libraries such as Firebase and Picasso.
 *
 * @see <a href="https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html">
 * The Firebase Blog: How does Firebase initialize on Android</a>
 */
class InitializationContentProvider : ContentProvider() {
  @Inject
  internal lateinit var functionalityHolder: FunctionalityHolder
  @Inject
  internal lateinit var observeImpl: Flowable<List<ListItem>>
  @Inject
  internal lateinit var refreshImpl: Refresh

  /**
   * This gets called on application creation and provides functionality implementations into the
   * holder, therefore allowing the domain layer to trigger functions without knowing their
   * implementations.
   */
  override fun onCreate() = true.also {
    componentF(context!!.applicationContext as Application).inject(this)
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

// DI root for this layer in the module. See dependencies.gradle for a more detailed explanation
private var componentF = { application: Application ->
  (application as ComponentProvider).moduleRootComponent(RootListComponent::class)
      .newInitializationContentProviderComponent()
}
