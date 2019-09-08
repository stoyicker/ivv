package list.presentation

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.include_list_view.content
import kotlinx.android.synthetic.main.include_list_view.error
import kotlinx.android.synthetic.main.include_list_view.progress
import kotlinx.android.synthetic.main.include_list_view.scroll_guide
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import list.RootListComponentHolder
import list.domain.ObserveCoordinator
import list.domain.RefreshCoordinator
import list.impl.ListItem
import org.jorge.test.list.R
import testaccessors.RequiresAccessor
import javax.inject.Inject

internal class ListActivity : AppCompatActivity(), ListViewInteractionListener {
  @Inject
  internal lateinit var observeCoordinator: ObserveCoordinator
  @Inject
  internal lateinit var refreshCoordinator: RefreshCoordinator
  @Inject
  internal lateinit var contentView: ContentView
  @Inject
  internal lateinit var listViewConfig: ListViewConfig
  @Inject
  internal lateinit var filterDelegate: FilterDelegate
  @Inject
  internal lateinit var pageLoadOnNext: PageLoadNextConsumer
  @Inject
  internal lateinit var pageLoadOnError: PageLoadErrorConsumer

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)
    setSupportActionBar(toolbar)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    if (intent.action == Intent.ACTION_SEARCH) {
      filterDelegate.applyQuery(intent.getStringExtra(SearchManager.QUERY))
    }
  }

  private fun onInjected() {
    listViewConfig.on(contentView)
    observeCoordinator.run(pageLoadOnNext, pageLoadOnError)
    refreshCoordinator()
    filterDelegate.apply {
      init(this@ListActivity)
      applyQuery(intent.getStringExtra(KEY_QUERY))
    }
  }

  /**
   * This gets called before a configuration change happens, so we use it to prevent leaking
   * the stream in the use case. It does not get called when the process finishes abnormally,
   * bun in that case there is no leak to worry about.
   */
  override fun onDestroy() {
    observeCoordinator.abort()
    // The refresh coordinator won't cause leaks, so we might as well always leave it complete
    // since the observe one will reuse its results upon next start
    super.onDestroy()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.list, menu)
    componentF(
        content,
        progress,
        error,
        scroll_guide,
        this,
        menu.findItem(R.id.search).actionView as SearchView)
        .inject(this)
    onInjected()
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.search) onSearchRequested()
    return super.onOptionsItemSelected(item)
  }

  override fun onStop() {
    intent.apply {
      if (isChangingConfigurations) {
        putExtra(KEY_QUERY, filterDelegate.query)
      } else {
        removeExtra(KEY_QUERY)
      }
    }
    super.onStop()
  }

  override fun onItemClicked(item: ListItem) =
      Toast.makeText(this, "TODO: Start detail activity", Toast.LENGTH_SHORT).show()

  override fun onPageLoadRequested() {
    contentView.apply {
      showLoadingLayout()
      hideErrorLayout()
    }
    refreshCoordinator()
  }
}

// DI root for this layer in the module. See dependencies.gradle for a more detailed explanation
@RequiresAccessor
internal var componentF = { contentView: RecyclerView,
                            progressView: View,
                            errorView: View,
                            guideView: View,
                            listener: ListViewInteractionListener,
                            searchView: SearchView ->
  RootListComponentHolder.rootListComponent.listActivityComponentBuilder()
      .contentView(contentView)
      .progressView(progressView)
      .errorView(errorView)
      .guideView(guideView)
      .listViewInteractionListener(listener)
      .searchView(searchView)
      .build()
}

fun listActivityIntent(context: Context) = Intent(context, ListActivity::class.java)
private const val KEY_QUERY = "KEY_QUERY"
