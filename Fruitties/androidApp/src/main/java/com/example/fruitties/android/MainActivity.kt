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

package com.example.fruitties.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.fruitties.android.ui.CartScreen
import com.example.fruitties.android.ui.FruittieScreen
import com.example.fruitties.android.ui.FruittiesTheme
import com.example.fruitties.android.ui.ListScreen
import kotlinx.serialization.Serializable

@Serializable
data object ListScreenKey : NavKey

@Serializable
data object CartScreenKey : NavKey

@Serializable
data class FruittieScreenKey(
    val id: Long,
) : NavKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalAppContainer provides (this.applicationContext as FruittiesAndroidApp).container,
            ) {
                FruittiesTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        NavApp()
                    }
                }
            }
        }
    }
}

@Composable
fun NavApp() {
    val backStack = rememberNavBackStack(ListScreenKey)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        onBack = { keysToRemove -> repeat(keysToRemove) { backStack.removeLastOrNull() } },
        entryProvider = entryProvider {
            entry<ListScreenKey> {
                ListScreen(
                    onFruittieClick = {
                        backStack.add(FruittieScreenKey(it.id))
                    },
                    onClickViewCart = {
                        backStack.add(CartScreenKey)
                    },
                )
            }
            entry<FruittieScreenKey> {
                FruittieScreen(
                    fruittieId = it.id,
                    onNavBarBack = {
                        backStack.removeIf { it is FruittieScreenKey }
                    },
                )
            }

            entry<CartScreenKey> {
                CartScreen(
                    onNavBarBack = {
                        backStack.removeIf { it is CartScreenKey }
                    },
                )
            }
        },
    )
}
