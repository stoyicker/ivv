package list.domain

internal class RefreshCoordinator(private val functionalityHolder: FunctionalityHolder) {
  private var nextPage = 1

  fun run() = functionalityHolder.refresh(nextPage++)
}
