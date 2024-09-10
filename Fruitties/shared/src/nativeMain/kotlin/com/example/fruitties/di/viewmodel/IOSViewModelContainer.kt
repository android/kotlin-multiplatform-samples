package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.fruitties.di.AppContainer
import com.example.fruitties.viewmodel.MainViewModel
import kotlin.reflect.KClass

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

fun <VM : ViewModel> viewModel(
    modelClass: KClass<VM>,
    viewModelStoreOwner: ViewModelStoreOwner,
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras = CreationExtras.Empty
): VM {
    val provider =
        if (factory != null) {
            ViewModelProvider.create(viewModelStoreOwner.viewModelStore, factory, extras)
        } else {
            ViewModelProvider.create(viewModelStoreOwner)
        }
    return if (key != null) {
        provider[key, modelClass]
    } else {
        provider[modelClass]
    }
}
