/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.fruitties.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.MainViewModel
import kotlin.reflect.KClass

enum class ViewModelType {
    MAIN,
    CART,
}

private fun selectViewModel(type: ViewModelType): Pair<KClass<out ViewModel>, ViewModelProvider.Factory?> =
    when (type) {
        ViewModelType.MAIN -> MainViewModel::class to MainViewModel.Factory
        ViewModelType.CART -> CartViewModel::class to CartViewModel.Factory
    }

/**
 * A ViewModelStoreOwner specifically for iOS.
 * This is used with from iOS with Kotlin Multiplatform (KMP).
 */
@Suppress("unused") // Android Studio is not aware of iOS usage.
class IOSViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()

    // Requires a type parameter because generics do not work well with KMP and Swift.
    // The caller must cast the ViewModel to the specific subclass.
    fun getViewModelWithEnum(
        type: ViewModelType,
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): ViewModel {
        val (kClass, defaultFactory) = selectViewModel(type)
        val provider =
            if (factory != null) {
                ViewModelProvider.create(this.viewModelStore, factory, extras ?: CreationExtras.Empty)
            } else if (defaultFactory != null) {
                ViewModelProvider.create(this.viewModelStore, defaultFactory, extras ?: CreationExtras.Empty)
            } else {
                ViewModelProvider.create(this)
            }
        return provider[kClass]
    }

    fun getMainViewModelWithFunctionUsingEnum(
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): MainViewModel =
        getViewModelWithEnum(
            type = ViewModelType.MAIN,
            factory = factory ?: MainViewModel.Factory,
            extras = extras ?: CreationExtras.Empty,
        ) as MainViewModel

    fun getMainViewModelWithFunctionUsingProvider(
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): MainViewModel =
        ViewModelProvider.create(
            owner = this as ViewModelStoreOwner,
            factory = factory ?: MainViewModel.Factory,
            extras = extras ?: CreationExtras.Empty,
        )[MainViewModel::class]

    fun getCartViewModelWithFunctionUsingEnum(
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): CartViewModel =
        getViewModelWithEnum(
            type = ViewModelType.CART,
            factory = factory ?: CartViewModel.Factory,
            extras = extras ?: CreationExtras.Empty,
        ) as CartViewModel

    fun getCartViewModelWithFunctionUsingProvider(
        factory: ViewModelProvider.Factory? = null,
        extras: CreationExtras? = null,
    ): CartViewModel =
        ViewModelProvider.create(
            owner = this as ViewModelStoreOwner,
            factory = factory ?: CartViewModel.Factory,
            extras = extras ?: CreationExtras.Empty,
        )[CartViewModel::class]

    // If the ViewModelStoreOwner will go out of scope, we should clear the ViewModelStore.
    fun clear() {
        viewModelStore.clear()
    }
}
