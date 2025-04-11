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
package com.example.fruitties.network

import com.example.fruitties.model.FruittiesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.coroutines.cancellation.CancellationException

interface FruittieApi {
    suspend fun getData(pageNumber: Int = 0): FruittiesResponse
}

class FruittieNetworkApi(
    private val client: HttpClient,
    private val apiUrl: String,
) : FruittieApi {
    override suspend fun getData(pageNumber: Int): FruittiesResponse {
        val url = "$apiUrl/$pageNumber.json"
        return try {
            client.get(url).body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()

            FruittiesResponse(emptyList(), 0, 0)
        }
    }
}
