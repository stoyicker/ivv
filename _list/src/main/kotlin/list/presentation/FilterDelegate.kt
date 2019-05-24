package list.presentation

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.support.v4.text.HtmlCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import org.jorge.test.list.R

/**
 * Contains boilerplate for list filtering.
 */
internal class FilterDelegate(
    private val searchView: SearchView, private val target: ListViewConfig) {
  internal var query: CharSequence = ""
    private set

  fun init(activity: Activity) {
    searchView.apply {
      setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = false

        override fun onQueryTextChange(newText: String?): Boolean {
          this@FilterDelegate.query = newText ?: ""
          target.filterView(query)
          return true
        }
      })
      setSearchableInfo((context.getSystemService(Context.SEARCH_SERVICE) as SearchManager)
          .getSearchableInfo(activity.componentName))
      setIconifiedByDefault(false)
      queryHint = HtmlCompat.fromHtml(context.getString(R.string.hint_search_view), 0)
    }
    activity.setDefaultKeyMode(AppCompatActivity.DEFAULT_KEYS_SEARCH_LOCAL)
  }

  /**
   * Delegates a query to the query handler in order to filter the list.
   * @param newQuery The query.
   */
  internal fun applyQuery(newQuery: CharSequence?) {
    searchView.setQuery(newQuery, false)
  }
}
