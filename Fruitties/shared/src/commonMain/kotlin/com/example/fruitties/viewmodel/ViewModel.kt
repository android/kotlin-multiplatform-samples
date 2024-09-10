package com.example.fruitties.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

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
