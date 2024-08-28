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
package com.example.fruitties

import com.example.fruitties.database.AppDatabase
import com.example.fruitties.database.Cart
import com.example.fruitties.database.CartDataStore
import com.example.fruitties.database.CartDetails
import com.example.fruitties.database.CartItemDetails
import com.example.fruitties.model.Fruittie
import com.example.fruitties.network.FruittieApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class DataRepository(
    private val api: FruittieApi,
    private var database: AppDatabase,
    private val cartDataStore: CartDataStore,
    private val scope: CoroutineScope,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val cartDetails: Flow<CartDetails>
        get() = cartDataStore.cart.mapLatest {
            val ids = it.items.map { it.id }
            val fruitties = database.fruittieDao().loadMapped(ids)
            CartDetails(
                items = it.items.mapNotNull {
                    fruitties[it.id]?.let { fruittie ->
                        CartItemDetails(fruittie, it.count)
                    }
                },
            )
        }

    suspend fun addToCart(fruittie: Fruittie) {
        cartDataStore.add(fruittie)
    }

    fun getCart(): Flow<Cart> {
        return cartDataStore.cart
    }

    fun getData(): Flow<List<Fruittie>> {
        scope.launch {
            if (database.fruittieDao().count() < 1) {
                refreshData()
            }
        }
        return loadData()
    }

    fun loadData(): Flow<List<Fruittie>> {
        return database.fruittieDao().getAllAsFlow()
    }

    suspend fun refreshData() {
        val response = api.getData()
        database.fruittieDao().insert(response.feed)
    }
}
