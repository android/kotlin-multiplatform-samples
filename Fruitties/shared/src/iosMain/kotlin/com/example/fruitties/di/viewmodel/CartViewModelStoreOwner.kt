package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.fruitties.di.AppContainer
import com.example.fruitties.viewmodel.CartViewModel

/**
 * A ViewModelStoreOwner specifically for iOS.
 * This is used with from iOS with Kotlin Multiplatform (KMP).
 */
@Suppress("unused") // Android Studio is not aware of iOS usage.
class CartViewModelStoreOwner(
    appContainer: AppContainer,
) : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()

    // Create an instance of CartViewModel with the CreationExtras.
    val cartViewModel: CartViewModel by lazy {
        ViewModelProvider.create(
            owner = this as ViewModelStoreOwner,
            factory = CartViewModel.Factory,
            extras = CartViewModel.newCreationExtras(appContainer),
        )[CartViewModel::class]
    }

    // If the ViewModelStoreOwner will go out of scope, we should clear the ViewModelStore.
    fun clear() {
        viewModelStore.clear()
    }
}
