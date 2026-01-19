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

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.fruitties.database.AppDatabase
import com.example.fruitties.database.CartDataStore
import com.example.fruitties.model.CartItemDetails
import com.example.fruitties.model.Fruittie
import com.example.fruitties.network.FruittieApi
import com.example.fruitties.paging.FruittieRemoteMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

class DataRepository(
    private val api: FruittieApi,
    private var database: AppDatabase,
    private val cartDataStore: CartDataStore,
    private val scope: CoroutineScope,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val cartDetails: Flow<List<CartItemDetails>>
        get() = cartDataStore.cart.mapLatest {
            val ids = it.items.map { it.id }
            val fruitties = database.fruittieDao().loadMapped(ids)
            it.items.mapNotNull {
                fruitties[it.id]?.let { fruittie ->
                    CartItemDetails(fruittie, it.count)
                }
            }
        }

    suspend fun addToCart(fruittie: Fruittie) {
        cartDataStore.add(fruittie)
    }

    suspend fun removeFromCart(fruittie: Fruittie) {
        cartDataStore.remove(fruittie)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingData(): Flow<PagingData<Fruittie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            remoteMediator = FruittieRemoteMediator(api, database),
            pagingSourceFactory = { database.fruittieDao().pagingSource() },
        ).flow

    suspend fun getFruittie(id: Long): Fruittie? = database.fruittieDao().getFruittie(id)

    fun fruittieInCart(id: Long): Flow<Int> =
        cartDataStore.cart.map { cart ->
            cart.items.find { it.id == id }?.count ?: 0
        }
}
