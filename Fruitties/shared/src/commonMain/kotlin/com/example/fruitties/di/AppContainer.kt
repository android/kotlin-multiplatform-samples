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
package com.example.fruitties.di

import com.example.fruitties.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.concurrent.Volatile

class AppContainer(
    private val factory: Factory,
) {
    val dataRepository: DataRepository by lazy {
        DataRepository(
            api = factory.createApi(),
            database = factory.createRoomDatabase(),
            cartDataStore = factory.createCartDataStore(),
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
        )
    }

    init {
        // Need a way to access the container from Kotlin Multiplatform module.
        // Create a global reference to the instantiated object.
        // This should be created once per Application, so there should only be 1 instance.
        // TODO(cartland): Check to see if there is a better way to access app global state.
        APP_CONTAINER_INSTANCE = this
    }
}

@Volatile
var APP_CONTAINER_INSTANCE: AppContainer? = null