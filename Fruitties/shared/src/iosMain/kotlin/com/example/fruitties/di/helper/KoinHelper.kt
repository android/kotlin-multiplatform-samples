/*
 * Copyright 2025 The Android Open Source Project
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

package com.example.fruitties.di.helper

import com.example.fruitties.di.appModule
import com.example.fruitties.di.multiplatformModule
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.FruittieViewModel
import com.example.fruitties.viewmodel.MainViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

fun initKoin() {
    startKoin {
        modules(appModule, multiplatformModule)
    }
}

class KoinHelper : KoinComponent {
    val mainViewModel: MainViewModel by inject()
    val cartViewModel: CartViewModel by inject()

    fun getFruittieViewModel(fruittieId: Long): FruittieViewModel {
        val fruittieViewModel: FruittieViewModel by inject { parametersOf(fruittieId) }
        return fruittieViewModel
    }
}
