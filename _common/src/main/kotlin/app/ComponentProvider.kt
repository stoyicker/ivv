package app

import kotlin.reflect.KClass

interface ComponentProvider {
  fun <T : Any> moduleRootComponent(componentClass: KClass<T>): T
}
