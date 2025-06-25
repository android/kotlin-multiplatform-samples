package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import kotlin.reflect.KClass

@Suppress("unused") // Android Studio is not aware of iOS usage.
@OptIn(BetaInteropApi::class)
fun ViewModelStore.getViewModel(
    modelClass: ObjCClass,
    factory: ViewModelProvider.Factory,
    key: String?,
    extras: CreationExtras,
): ViewModel {
    @Suppress("UNCHECKED_CAST")
    val vmClass = getOriginalKotlinClass(modelClass) as? KClass<ViewModel>
        ?: error("modelClass isn't a ViewModel type")
    val provider = ViewModelProvider.create(this, factory, extras)
    return key?.let { provider[key, vmClass] } ?: provider[vmClass]
}