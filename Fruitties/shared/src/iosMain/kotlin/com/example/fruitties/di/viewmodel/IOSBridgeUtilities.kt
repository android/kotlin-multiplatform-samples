package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras

interface IOSViewModelStoreOwner {
    val viewModelStore: IOSViewModelStore
}

data class IOSViewModelProviderFactory(
    val factory: ViewModelProvider.Factory,
)

data class IOSViewModelStore(
    val store: ViewModelStore,
)

data class IOSCreationExtras(
    val extras: CreationExtras,
)

@Suppress("unused")
fun createIOSViewModelStore(): IOSViewModelStore = IOSViewModelStore(ViewModelStore())

@Suppress("unused")
fun createViewModelProvider(
    owner: IOSViewModelStoreOwner,
    factory: IOSViewModelProviderFactory,
    extras: IOSCreationExtras,
): ViewModelProvider = ViewModelProvider.create(owner.viewModelStore.store, factory.factory, extras.extras)
