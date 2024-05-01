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

package com.example.fruitties.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fruitties.DataRepository
import com.example.fruitties.android.di.App
import com.example.fruitties.database.CartItemDetails
import com.example.fruitties.model.Fruittie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        repository.getData().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    val cartUiState: StateFlow<CartUiState> =
        repository.cartDetails.map { CartUiState(it.items) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CartUiState()
            )

    fun addItemToCart(fruittie: Fruittie) {
        viewModelScope.launch {
            repository.addToCart(fruittie)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as App)
                val repository = application.container.dataRepository
                MainViewModel(repository = repository)
            }
        }
    }
}

/**
 * Ui State for ListScreen
 */
data class HomeUiState(val itemList: List<Fruittie> = listOf())

/**
 * Ui State for Cart
 */
data class CartUiState(val itemList: List<CartItemDetails> = listOf())

private const val TIMEOUT_MILLIS = 5_000L
