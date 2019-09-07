package list.domain

internal object RefreshCoordinator : () -> Unit {
  private var nextPage = 1

  override fun invoke() = FunctionalityHolder.refresh(nextPage++)
}
