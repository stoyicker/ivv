package list.domain

import testaccessors.RequiresAccessor

class RefreshCoordinator(private val functionalityHolder: FunctionalityHolder)
  : () -> Unit {
  @RequiresAccessor(requires = [RequiresAccessor.AccessorType.TYPE_GETTER])
  private var nextPage = 1

  override fun invoke() = functionalityHolder.refresh(nextPage++)
}
