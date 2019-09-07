package list.presentation

import android.support.v7.widget.RecyclerView
import android.view.View
import dagger.Module
import dagger.Provides

@Module
internal class ContentViewModule {
  @Provides
  @ListActivityScope
  fun contentView(recyclerView: RecyclerView,
                  @Error
                  errorView: View,
                  @Progress
                  progressView: View,
                  @Guide
                  guideView: View) = ContentView(recyclerView, errorView, progressView, guideView)
}
