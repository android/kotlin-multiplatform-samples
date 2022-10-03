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

import kotlin.random.Random
import kotlin.random.nextInt

class DiceRoller {
    @Throws(IllegalArgumentException::class)
    fun rollDice(settings: DiceSettings): List<Int> {
        require(settings.diceCount >= 1) {
            "Must throw a positive number of dice (tried to roll ${settings.diceCount})"
        }
        require(settings.sideCount >= 3) {
            "Dice must have at least 3 sides (tried to roll ${settings.sideCount}-sided dice)"
        }

        return if (!settings.uniqueRollsOnly) {
            // Just roll the given number of dice and return results
            List(settings.diceCount) { Random.nextInt(1..settings.sideCount) }
        } else {
            require(settings.diceCount <= settings.sideCount) {
                "Can't roll ${settings.diceCount} unique values with ${settings.sideCount}-side dice"
            }

            buildList {
                // Create set of available numbers
                val availableNumbers = (1..settings.sideCount).toMutableSet()
                // Draw numbers from set
                repeat(settings.diceCount) {
                    val next = availableNumbers.random().also { availableNumbers.remove(it) }
                    add(next)
                }
            }
        }
    }
}
