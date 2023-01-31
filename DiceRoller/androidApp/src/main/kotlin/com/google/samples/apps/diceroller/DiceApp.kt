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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DiceApp(viewModel: DiceViewModel) {
    val settingsState by viewModel.settings.collectAsStateWithLifecycle()
    val resultState by viewModel.result.collectAsStateWithLifecycle()

    val settings = settingsState

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.app_name)) }) },
    ) { paddingValues ->
        if (settings == null) {
            Text(stringResource(R.string.loading_settings))
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {
                GameArea(viewModel, settings, resultState, modifier = Modifier.weight(1f))
                Divider(Modifier.padding(16.dp))
                Settings(viewModel, settings)
            }
        }
    }
}

@Composable
private fun GameArea(
    viewModel: DiceViewModel,
    settings: DiceSettings,
    resultState: DiceRollResult,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { viewModel.rollDice() }) {
            val text = buildString {
                @OptIn(ExperimentalComposeUiApi::class)
                append(
                    pluralStringResource(
                        id = R.plurals.roll_button_text,
                        count = settings.diceCount,
                        settings.diceCount,
                        settings.sideCount,
                    )
                )
                if (settings.uniqueRollsOnly) {
                    appendLine()
                    append(stringResource(R.string.roll_button_text_unique_suffix))
                }
            }

            @OptIn(ExperimentalAnimationApi::class)
            AnimatedContent(text) {
                Text(text = text, textAlign = TextAlign.Center)
            }
        }
        Text(
            text = when (resultState) {
                DiceRollResult.Error -> stringResource(R.string.roll_result_error)
                DiceRollResult.Initial -> stringResource(R.string.roll_result_waiting)
                is DiceRollResult.Success -> stringResource(
                    R.string.roll_result,
                    resultState.values.sum(),
                    resultState.values.joinToString()
                )
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
private fun Settings(
    viewModel: DiceViewModel,
    settings: DiceSettings,
    modifier: Modifier = Modifier,
) {
    var diceCount by remember { mutableStateOf(settings.diceCount) }
    var sideCount by remember { mutableStateOf(settings.sideCount) }
    var uniqueRollsOnly by remember { mutableStateOf(settings.uniqueRollsOnly) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.configure_roll_settings), fontWeight = FontWeight.Bold)

        val unsavedNumber = diceCount != settings.diceCount
        ValueChooser(
            text = stringResource(R.string.number_of_dice, diceCount),
            value = diceCount,
            onValueChange = { diceCount = it },
            unsaved = unsavedNumber,
            validRange = 1..10,
        )

        val unsavedSides = sideCount != settings.sideCount
        ValueChooser(
            text = stringResource(R.string.sides_of_dice, sideCount),
            value = sideCount,
            onValueChange = { sideCount = it },
            unsaved = unsavedSides,
            validRange = 3..100,
        )

        val unsavedUnique = uniqueRollsOnly != settings.uniqueRollsOnly
        CheckboxChooser(
            text = stringResource(R.string.unique_rolls),
            value = uniqueRollsOnly,
            onValueChange = { uniqueRollsOnly = it },
            unsaved = unsavedUnique,
        )

        Button(
            onClick = { viewModel.saveSettings(diceCount, sideCount, uniqueRollsOnly) },
            enabled = unsavedNumber || unsavedSides || unsavedUnique,
        ) {
            Text(stringResource(R.string.save_settings))
        }
    }
}

@Composable
private fun ValueChooser(
    text: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    unsaved: Boolean,
    validRange: IntRange,
    modifier: Modifier = Modifier,
) {
    val buttonModifier = Modifier
        .padding(8.dp)
        .size(48.dp)

    Row(
        modifier = modifier.width(300.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = { onValueChange((value - 1).coerceIn(validRange)) },
            modifier = buttonModifier,
        ) {
            Text(text = "-")
        }

        Text(text = if (unsaved) "$text*" else text)

        Button(
            onClick = { onValueChange((value + 1).coerceIn(validRange)) },
            modifier = buttonModifier,
        ) {
            Text(text = "+")
        }
    }
}

@Composable
private fun CheckboxChooser(
    text: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    unsaved: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.width(300.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(checked = value, onCheckedChange = onValueChange)
        Text(text, modifier = Modifier.clickable { onValueChange(!value) })
        Text("*", modifier = Modifier.alpha(if (unsaved) 1f else 0f))
    }
}
