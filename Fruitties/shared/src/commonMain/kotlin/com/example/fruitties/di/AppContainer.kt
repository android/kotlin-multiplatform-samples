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

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fruitties.DataRepository
import com.example.fruitties.network.FruittieApi
import com.example.fruitties.network.FruittieNetworkApi
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.FruittieViewModel
import com.example.fruitties.viewmodel.FruittieViewModel.Companion.FRUITTIE_ID_KEY
import com.example.fruitties.viewmodel.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

val json = Json { ignoreUnknownKeys = true }

class AppContainer(
    private val factory: Factory,
) {
    val dataRepository: DataRepository by lazy {
        DataRepository(
            api = commonCreateApi(),
            database = factory.createRoomDatabase(),
            cartDataStore = factory.createCartDataStore(),
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
        )
    }

    val mainViewModelFactory = viewModelFactory {
        initializer {
            MainViewModel(repository = dataRepository)
        }
    }

    val cartViewModelFactory = viewModelFactory {
        initializer {
            CartViewModel(repository = dataRepository)
        }
    }

    val fruittieViewModelFactory = viewModelFactory {
        initializer {
            // this: CreationExtras
            FruittieViewModel(
                fruittieId = this[FRUITTIE_ID_KEY] ?: error("Expected fruittieId!"),
                repository = dataRepository,
            )
        }
    }

    internal fun commonCreateApi(): FruittieApi =
        FruittieNetworkApi(
            client = HttpClient {
                install(ContentNegotiation) {
                    json(json, contentType = ContentType.Any)
                }
            },
            apiUrl = "https://android.github.io/kotlin-multiplatform-samples/fruitties-api",
        )
}
