package app

import android.annotation.SuppressLint
import android.app.Application
import tmdb.RootTmdbComponent
import splash.RootSplashComponent
import kotlin.reflect.KClass

@SuppressLint("Registered") // It is registered in the manifest
internal open class MyApplication : Application(), ComponentProvider {
  private val rootComponent by lazy { DaggerRootComponent.factory().create(this) }
  private val rootSplashComponent by lazy { rootComponent.newRootSplashComponent() }
  private val rootListComponent by lazy { rootComponent.newRootTmdbComponent() }

  @Suppress("UNCHECKED_CAST")
  override fun <T : Any> moduleRootComponent(componentClass: KClass<T>) = when (componentClass) {
    RootSplashComponent::class -> rootSplashComponent as T
    RootTmdbComponent::class -> rootListComponent as T
    else -> throw IllegalArgumentException("Illegal module root component class requested: ${componentClass.java.canonicalName}")
  }
}
