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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fruitties.DataRepository
import com.example.fruitties.di.AppContainer
import com.example.fruitties.model.Fruittie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: DataRepository,
) : ViewModel() {
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

    companion object {
        val APP_CONTAINER_KEY = CreationExtras.Key<AppContainer>()

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appContainer = this[APP_CONTAINER_KEY] as AppContainer
                val repository = appContainer.dataRepository
                MainViewModel(repository = repository)
            }
        }

        @Suppress("unused")
        val KClass = MainViewModel::class

        /**
         * Helper function to prepare CreationExtras.
         *
         * USAGE:
         *
         * val mainViewModel: MainViewModel = ViewModelProvider.create(
         *  owner = this as ViewModelStoreOwner,
         *  factory = MainViewModel.Factory,
         *  extras = MainViewModel.newCreationExtras(appContainer),
         * )[MainViewModel::class]
         */
        fun creationExtras(appContainer: AppContainer): CreationExtras =
            MutableCreationExtras().apply {
                set(APP_CONTAINER_KEY, appContainer)
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
