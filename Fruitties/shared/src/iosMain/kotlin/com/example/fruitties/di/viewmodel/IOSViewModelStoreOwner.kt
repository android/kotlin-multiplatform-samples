package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.MainViewModel

/**
 * A ViewModelStoreOwner specifically for iOS.
 * This is used with from iOS with Kotlin Multiplatform (KMP).
 */
@Suppress("unused") // Android Studio is not aware of iOS usage.
class IOSViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()

    fun getMainViewModel(
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): MainViewModel =
        ViewModelProvider.create(
            owner = this as ViewModelStoreOwner,
            factory = MainViewModel.Factory,
            extras = extras ?: CreationExtras.Empty,
        )[MainViewModel::class]

    fun getCartViewModel(
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): CartViewModel =
        ViewModelProvider.create(
            owner = this as ViewModelStoreOwner,
            factory = CartViewModel.Factory,
            extras = extras ?: CreationExtras.Empty,
        )[CartViewModel::class]

    // If the ViewModelStoreOwner will go out of scope, we should clear the ViewModelStore.
    fun clear() {
        viewModelStore.clear()
    }
}
