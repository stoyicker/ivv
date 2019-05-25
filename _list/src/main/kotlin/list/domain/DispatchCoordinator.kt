package list.domain

internal class RefreshCoordinator(private val functionalityHolder: FunctionalityHolder)
  : IRefreshCoordinator {
  private var nextPage = 1

  override fun run() = functionalityHolder.refresh(nextPage++)
}

// For testing
internal interface IRefreshCoordinator {
  fun run()
}
