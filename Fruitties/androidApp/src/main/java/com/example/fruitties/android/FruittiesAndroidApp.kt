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

package com.example.fruitties.android

import android.app.Application
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.fruitties.di.AppContainer
import com.example.fruitties.di.Factory

class FruittiesAndroidApp : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(Factory(this))
    }
}

/**
 * Allows retrieving the AppContainer, which represents a DI graph everywhere from a composable.
 * Because the [AppContainer] is effectively a singleton, we can use static composition local,
 * because it won't change during the app execution.
 */
val LocalAppContainer =
    staticCompositionLocalOf<AppContainer> { error("No AppContainer provided!") }
