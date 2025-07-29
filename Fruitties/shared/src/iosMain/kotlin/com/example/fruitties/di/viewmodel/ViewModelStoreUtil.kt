package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import kotlin.reflect.KClass

/**
 * This function allows retrieving any ViewModel from Swift Code with generics.
 * We only get [ObjCClass] type for the [modelClass], because the interop between Kotlin and Swift
 * code doesn't preserve the generic class, but we can retrieve the original KClass in Kotlin.
 */
@Suppress("unused") // Android Studio is not aware of iOS usage.
@OptIn(BetaInteropApi::class)
@Throws(IllegalStateException::class)
fun ViewModelStoreOwner.viewModel(
    modelClass: ObjCClass,
    factory: ViewModelProvider.Factory,
    key: String?,
    extras: CreationExtras? = null,
): ViewModel {
    @Suppress("UNCHECKED_CAST")
    val vmClass = getOriginalKotlinClass(modelClass) as? KClass<ViewModel>
        ?: error("modelClass isn't a ViewModel type")
    val provider = ViewModelProvider.create(this, factory, extras ?: CreationExtras.Empty)
    return key?.let { provider[key, vmClass] } ?: provider[vmClass]
}
