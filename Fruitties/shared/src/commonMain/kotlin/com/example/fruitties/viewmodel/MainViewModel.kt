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
import com.example.fruitties.model.Fruittie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: DataRepository,
) : ViewModel() {
    init {
        Logger.v { "MainViewModel created" }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v { "MainViewModel cleared" }
    }

    val homeUiState: StateFlow<HomeUiState> =
        repository
            .getData()
            .combine(repository.cartDetails) { fruitties, cartState ->
                HomeUiState(
                    fruitties = fruitties,
                    cartItemCount = cartState.sumOf { item -> item.count },
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState(),
            )

    fun addItemToCart(fruittie: Fruittie) {
        viewModelScope.launch {
            repository.addToCart(fruittie)
        }
    }
}

/**
 * Ui State for the home screen
 */
data class HomeUiState(
    val fruitties: List<Fruittie> = listOf(),
    val cartItemCount: Int = 0,
)

private const val TIMEOUT_MILLIS = 5_000L
