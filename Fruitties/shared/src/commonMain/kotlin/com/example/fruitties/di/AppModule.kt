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

package com.example.fruitties.di

import com.example.fruitties.DataRepository
import com.example.fruitties.network.FruittieApi
import com.example.fruitties.network.FruittieNetworkApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    single<FruittieApi> {
        commonCreateApi()
    }
    single<CoroutineScope> {
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
    factoryOf(::DataRepository)
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

val json = Json { ignoreUnknownKeys = true }
