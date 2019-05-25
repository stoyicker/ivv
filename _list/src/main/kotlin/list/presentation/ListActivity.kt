package list.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.include_list_view.content
import kotlinx.android.synthetic.main.include_list_view.error
import kotlinx.android.synthetic.main.include_list_view.progress
import kotlinx.android.synthetic.main.include_list_view.scroll_guide
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import list.SchedulerModule
import list.domain.DomainModule
import list.domain.FunctionalityHolderModule
import list.domain.ObserveCoordinator
import list.domain.RefreshCoordinator
import list.impl.ListItem
import org.jorge.test.list.R
import javax.inject.Inject

class ListActivity : AppCompatActivity(), ListViewInteractionListener {
  // DI root for this layer in this module. See dependencies.gradle for a more detailed explanation
  private val componentF = { contentView: RecyclerView,
                             progressView: View,
                             errorView: View,
                             guideView: View,
                             listener: ListViewInteractionListener,
                             searchView: SearchView ->
    DaggerListActivityComponent.builder()
        .contentViewModule(ContentViewModule)
        .filterModule(FilterModule)
        .functionalityHolderModule(FunctionalityHolderModule)
        .domainModule(DomainModule)
        .schedulerModule(SchedulerModule)
        .contentView(contentView)
        .progressView(progressView)
        .errorView(errorView)
        .consumerModule(ConsumerModule)
        .guideView(guideView)
        .listViewInteractionListener(listener)
        .searchView(searchView)
        .build() as DaggerListActivityComponent
  }
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

  private fun onInjected() {
    listViewConfig.on(contentView)
    observeCoordinator.run(pageLoadOnNext, pageLoadOnError)
    if (intent.getBooleanExtra(KEY_REFRESH, true)) {
      refreshCoordinator.run()
    }
    filterDelegate.apply {
      init(this@ListActivity)
      applyQuery(intent.getStringExtra(KEY_QUERY))
    }
  }

  /**
   * This gets called before a configuration change happens, so we use it to prevent leaking
   * the observable in the use case. It does not get called when the process finishes abnormally,
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
        menu.findItem(R.id.search).actionView as SearchView).inject(this)
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
        putExtra(KEY_REFRESH, false)
      } else {
        removeExtra(KEY_QUERY)
        removeExtra(KEY_REFRESH)
      }
    }
    super.onStop()
  }

  override fun onItemClicked(item: ListItem) = TODO("Start detail activity")

  override fun onPageLoadRequested() {
    contentView.apply {
      showLoadingLayout()
      hideContentLayout()
      hideErrorLayout()
    }
    observeCoordinator.nextPage()
  }
}

fun listActivityIntent(context: Context) = Intent(context, ListActivity::class.java)
private const val KEY_QUERY = "KEY_QUERY"
private const val KEY_REFRESH = "KEY_REFRESH"
