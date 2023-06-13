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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DiceSettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        const val DEFAULT_DICE_COUNT = 2
        const val DEFAULT_SIDES_COUNT = 6
        const val DEFAULT_UNIQUE_ROLLS_ONLY = false
    }

    private val scope = CoroutineScope(Dispatchers.Default)

    private val diceCountKey = intPreferencesKey("dice_count")
    private val sideCountKey = intPreferencesKey("side_count")
    private val uniqueRollsOnlyKey = booleanPreferencesKey("unique_rolls_only")

    @NativeCoroutines
    val settings: Flow<DiceSettings> = dataStore.data.map {
        DiceSettings(
            it[diceCountKey] ?: DEFAULT_DICE_COUNT,
            it[sideCountKey] ?: DEFAULT_SIDES_COUNT,
            it[uniqueRollsOnlyKey] ?: DEFAULT_UNIQUE_ROLLS_ONLY,
        )
    }

    fun saveSettings(
        diceCount: Int,
        sideCount: Int,
        uniqueRollsOnly: Boolean,
    ) {
        scope.launch {
            dataStore.edit {
                it[diceCountKey] = diceCount
                it[sideCountKey] = sideCount
                it[uniqueRollsOnlyKey] = uniqueRollsOnly
            }
        }
    }
}
