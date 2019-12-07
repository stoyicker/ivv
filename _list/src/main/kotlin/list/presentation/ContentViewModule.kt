package list.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
