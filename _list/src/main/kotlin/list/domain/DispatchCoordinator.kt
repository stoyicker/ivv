package list.domain

internal class DispatchCoordinator(private val functionalityHolder: FunctionalityHolder) {
  fun run() = functionalityHolder.dispatch()
}
