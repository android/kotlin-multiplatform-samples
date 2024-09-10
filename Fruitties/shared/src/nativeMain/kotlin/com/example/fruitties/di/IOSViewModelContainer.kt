package com.example.fruitties.di

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.fruitties.viewmodel.MainViewModel
import com.example.fruitties.viewmodel.viewModel

// TODO: Find a better ViewModelStoreOwner for iOS.
class IOSViewModelContainer(private val appContainer: AppContainer) : ViewModelStoreOwner {
    private var _viewModelStore: ViewModelStore? = null
    override val viewModelStore: ViewModelStore
        get() = _viewModelStore ?: ViewModelStore().also {
            this._viewModelStore = it
        }

    // Experimenting with ViewModel on iOS.
    val mainViewModel: MainViewModel = viewModel(
        modelClass = MainViewModel::class,
        viewModelStoreOwner = this,
        factory = MainViewModel.Factory,
        extras = MutableCreationExtras().apply {
            set(MainViewModel.APP_CONTAINER_KEY, appContainer)
        } as CreationExtras,
    )

    // TODO: Clear the ViewModelStore.
    fun clearViewModelStore() {
        viewModelStore.clear()
    }
}
