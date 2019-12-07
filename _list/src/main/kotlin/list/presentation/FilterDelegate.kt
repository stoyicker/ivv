package list.presentation

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.text.HtmlCompat
import org.jorge.test.list.R

/**
 * Contains boilerplate for list filtering.
 */
class FilterDelegate(
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
  fun applyQuery(newQuery: CharSequence?) = searchView.setQuery(newQuery, false)
}
