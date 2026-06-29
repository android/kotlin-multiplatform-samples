/*
 * Copyright 2026 The Android Open Source Project
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
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.fruitties.DataRepository
import com.example.fruitties.model.Fruittie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FruittieViewModel(
    private val fruittieId: Long,
    private val repository: DataRepository,
) : ViewModel() {
    sealed class State {
        data object Loading : State()

        data class Content(
            val inCart: Int,
            val fruittie: Fruittie,
        ) : State()
    }

    val state = combine(
        flow { emit(repository.getFruittie(fruittieId)) }.filterNotNull(),
        repository.fruittieInCart(fruittieId),
    ) { fruittie, inCart ->
        State.Content(inCart, fruittie)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Loading,
    )

    fun addToCart(fruittie: Fruittie) {
        viewModelScope.launch {
            repository.addToCart(fruittie)
        }
    }

    companion object {
        val FRUITTIE_ID_KEY = CreationExtras.Key<Long>()
    }
}
