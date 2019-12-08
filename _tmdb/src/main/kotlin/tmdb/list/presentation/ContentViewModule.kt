package tmdb.list.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides

@Module
object ContentViewModule {
  @Provides
  @ListActivityScope
  @JvmStatic
  fun contentView(recyclerView: RecyclerView,
                  @Error
                  errorView: View,
                  @Progress
                  progressView: View,
                  @Guide
                  guideView: View) = ContentView(recyclerView, errorView, progressView, guideView)
}
