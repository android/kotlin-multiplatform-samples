/*
 * Copyright 2022 The Android Open Source Project
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
package com.google.samples.apps.diceroller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class DiceViewModel(
    private val roller: DiceRoller,
    private val settingsRepository: DiceSettingsRepository,
) : ViewModel() {
    private val _result = MutableStateFlow<DiceRollResult>(DiceRollResult.Initial)
    val result: StateFlow<DiceRollResult> = _result.asStateFlow()

    val settings: StateFlow<DiceSettings?> = settingsRepository
        .settings
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            null
        )

    fun saveSettings(
        number: Int,
        sides: Int,
        unique: Boolean,
    ) = settingsRepository.saveSettings(number, sides, unique)

    fun rollDice() {
        // Ignore attempted rolls before settings are available
        val settings = settings.value ?: return

        _result.value = try {
            DiceRollResult.Success(roller.rollDice(settings))
        } catch (e: IllegalArgumentException) {
            DiceRollResult.Error
        }
    }
}

sealed interface DiceRollResult {
    object Initial : DiceRollResult
    class Success(val values: List<Int>) : DiceRollResult
    object Error : DiceRollResult
}
