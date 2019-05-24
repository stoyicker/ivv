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
import list.domain.DispatchCoordinator
import list.domain.DomainModule
import list.domain.ObserveCoordinator
import org.jorge.test.list.R
import testaccessors.RequiresAccessor
import javax.inject.Inject

internal class ListActivity : AppCompatActivity(), ListViewInteractionListener {
  // DI root for this layer in this module. See dependencies.gradle for a more detailed explanation
  @RequiresAccessor
  private val componentF = { contentView: RecyclerView,
                             progressView: View,
                             errorView: View,
                             guideView: View,
                             listener: ListViewInteractionListener,
                             searchView: SearchView ->
    DaggerListActivityComponent.builder()
        .contentViewModule(ContentViewModule)
        .filterModule(FilterModule)
        .domainModule(DomainModule)
        .schedulerModule(SchedulerModule)
        .contentView(contentView)
        .progressView(progressView)
        .errorView(errorView)
        .guideView(guideView)
        .listViewInteractionListener(listener)
        .searchView(searchView)
        .build()
  }
  @Inject
  lateinit var observeCoordinator: ObserveCoordinator
  @Inject
  lateinit var dispatchCoordinator: DispatchCoordinator
  @Inject
  lateinit var contentView: ContentView
  @Inject
  lateinit var listViewConfig: ListViewConfig
  @Inject
  lateinit var filterDelegate: FilterDelegate
  @Inject
  lateinit var pageLoadOnNext: PageLoadNextConsumer
  @Inject
  lateinit var pageLoadOnError: PageLoadErrorConsumer

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)
    setSupportActionBar(toolbar)
  }

  private fun onInjected() {
    listViewConfig.on(contentView)
    observeCoordinator.run(pageLoadOnNext, pageLoadOnError)
    dispatchCoordinator.run()
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
    // The dispatch coordinator won't cause leaks, so we might as well always leave it complete
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
    if (isChangingConfigurations) {
      intent.putExtra(KEY_QUERY, filterDelegate.query)
    }
    super.onStop()
  }

  override fun onItemClicked(item: PresentationItem) = TODO("Start detail activity")

  override fun onPageLoadRequested() = observeCoordinator.nextPage()
}

fun listActivityIntent(context: Context) = Intent(context, ListActivity::class.java)
private const val KEY_QUERY = "KEY_QUERY"
