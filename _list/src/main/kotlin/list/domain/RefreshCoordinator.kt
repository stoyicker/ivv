package list.domain

internal object RefreshCoordinator : IRefreshCoordinator {
  private var nextPage = 1

  override fun run() = FunctionalityHolder.refresh(nextPage++)
}

// For testing
internal interface IRefreshCoordinator {
  fun run()
}
