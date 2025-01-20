package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.fruitties.di.AppContainer
import com.example.fruitties.viewmodel.MainViewModel

/**
 * A ViewModelStoreOwner specifically for iOS.
 * This is used with from iOS with Kotlin Multiplatform (KMP).
 */
@Suppress("unused") // Android Studio is not aware of iOS usage.
class IOSViewModelOwner(appContainer: AppContainer) : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()

    // Create an instance of MainViewModel with the CreationExtras.
    val mainViewModel: MainViewModel = ViewModelProvider.create(
        owner = this as ViewModelStoreOwner,
        factory = MainViewModel.Factory,
        extras = MainViewModel.newCreationExtras(appContainer),
    )[MainViewModel::class]

    // To add more ViewModel types, add new properties for each ViewModel.
    // If we need to add a very large number of ViewModel types,
    // we could consider creating a generic retrieval implementation with reflection.

    // If the ViewModelStoreOwner will go out of scope, we should clear the ViewModelStore.
    fun clear() {
        viewModelStore.clear()
    }
}
