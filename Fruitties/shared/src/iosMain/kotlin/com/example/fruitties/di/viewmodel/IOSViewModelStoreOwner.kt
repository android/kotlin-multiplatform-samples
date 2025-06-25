package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.MainViewModel
import kotlin.reflect.KClass

enum class ViewModelType {
    MAIN,
    CART,
}

private fun selectViewModel(type: ViewModelType): Pair<KClass<out ViewModel>, ViewModelProvider.Factory?> =
    when (type) {
        ViewModelType.MAIN -> MainViewModel::class to MainViewModel.Factory
        ViewModelType.CART -> CartViewModel::class to CartViewModel.Factory
    }

/**
 * A ViewModelStoreOwner specifically for iOS.
 * This is used with from iOS with Kotlin Multiplatform (KMP).
 */
@Suppress("unused") // Android Studio is not aware of iOS usage.
class IOSViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()

    fun getViewModel(
        // Requires a type parameter because generics do not work well with KMP and Swift.
        // The caller must cast the ViewModel to the specific subclass.
        type: ViewModelType,
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): ViewModel {
        val (kClass, defaultFactory) = selectViewModel(type)
        val provider =
            if (factory != null) {
                ViewModelProvider.create(this.viewModelStore, factory, extras ?: CreationExtras.Empty)
            } else if (defaultFactory != null) {
                ViewModelProvider.create(this.viewModelStore, defaultFactory, extras ?: CreationExtras.Empty)
            } else {
                ViewModelProvider.create(this)
            }
        return provider[kClass]
    }

    // Type-safe, but requires a new function name.
    fun getMainViewModel(
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): MainViewModel =
        getViewModel(
            type = ViewModelType.MAIN,
            factory = factory ?: MainViewModel.Factory,
            extras = extras ?: CreationExtras.Empty,
        ) as MainViewModel
    // This also works:
//        ViewModelProvider.create(
//            owner = this as ViewModelStoreOwner,
//            factory = factory ?: MainViewModel.Factory,
//            extras = extras ?: CreationExtras.Empty,
//        )[MainViewModel::class]

    fun getCartViewModel(
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): CartViewModel =
        getViewModel(
            type = ViewModelType.CART,
            factory = factory ?: CartViewModel.Factory,
            extras = extras ?: CreationExtras.Empty,
        ) as CartViewModel
    // This also works:
//        ViewModelProvider.create(
//            owner = this as ViewModelStoreOwner,
//            factory = factory ?: CartViewModel.Factory,
//            extras = extras ?: CreationExtras.Empty,
//        )[CartViewModel::class]

    // If the ViewModelStoreOwner will go out of scope, we should clear the ViewModelStore.
    fun clear() {
        viewModelStore.clear()
    }
}
