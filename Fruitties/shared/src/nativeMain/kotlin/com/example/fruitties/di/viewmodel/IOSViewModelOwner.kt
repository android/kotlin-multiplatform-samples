package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.fruitties.di.AppContainer
import com.example.fruitties.viewmodel.MainViewModel

class IOSViewModelOwner(private val appContainer: AppContainer) : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()

    // Prepare the parameters for MainViewModel with CreationExtras
    private val mainViewModelExtras: CreationExtras = MutableCreationExtras().apply {
        set(MainViewModel.APP_CONTAINER_KEY, appContainer)
    }

    val mainViewModel: MainViewModel = ViewModelProvider.create(
        this as ViewModelStoreOwner,
        MainViewModel.Factory,
        mainViewModelExtras,
    )[MainViewModel::class]

    fun clear() {
        viewModelStore.clear()
    }
}
