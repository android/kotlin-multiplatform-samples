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

package com.example.fruitties.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.fruitties.DataRepository
import com.example.fruitties.model.CartItemDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: DataRepository,
) : ViewModel() {
    init {
        // Leaving the comments here to show when the ViewModel is created.
        Logger.v { "CartViewModel created" }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v { "CartViewModel cleared" }
    }

    val cartUiState: StateFlow<CartUiState> =
        repository.cartDetails
            .map { details ->
                CartUiState(cartDetails = details)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CartUiState(),
            )

    fun increaseCountClick(cartItem: CartItemDetails) {
        viewModelScope.launch {
            repository.addToCart(cartItem.fruittie)
        }
    }

    fun decreaseCountClick(cartItem: CartItemDetails) {
        viewModelScope.launch {
            repository.removeFromCart(cartItem.fruittie)
        }
    }
}

/**
 * Ui State for the cart
 */
data class CartUiState(
    val cartDetails: List<CartItemDetails> = listOf(),
) {
    val totalItemCount: Int = cartDetails.sumOf { it.count }
}

private const val TIMEOUT_MILLIS = 5_000L
