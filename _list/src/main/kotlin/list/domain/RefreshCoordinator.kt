package list.domain

internal class RefreshCoordinator : () -> Unit {
  private var nextPage = 1

  override fun invoke() = FunctionalityHolder.refresh(nextPage++)
}
