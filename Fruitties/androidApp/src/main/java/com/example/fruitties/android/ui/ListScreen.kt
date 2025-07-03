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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fruitties.android.R
import com.example.fruitties.android.di.App
import com.example.fruitties.model.Fruittie
import com.example.fruitties.viewmodel.MainViewModel
import com.example.fruitties.viewmodel.creationExtras

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(onClickViewCart: () -> Unit = {}) {
    // Instantiate a ViewModel with a dependency on the AppContainer.
    // To make ViewModel compatible with KMP, the ViewModel factory must
    // create an instance without referencing the Android Application.
    // Here we put the KMP-compatible AppContainer into the extras
    // so it can be passed to the ViewModel factory.
    val app = LocalContext.current.applicationContext as App
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModel.Factory,
        extras = creationExtras(app.container),
    )

    val uiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.frutties))
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing.only(
            // Do not include Bottom so scrolled content is drawn below system bars.
            // Include Horizontal because some devices have camera cutouts on the side.
            WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
        ),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                // Support edge-to-edge (required on Android 15)
                // https://developer.android.com/develop/ui/compose/layouts/insets#inset-size
                .padding(
                    // Draw to bottom edge. LazyColumn adds a Spacer for WindowInsets.systemBars.
                    // No bottom padding.
                    top = paddingValues.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onClickViewCart,
                modifier = Modifier.padding(8.dp),
            ) {
                Text(text = "View Cart (${uiState.cartItemCount})")
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(64.dp),
            ) {
                items(items = uiState.fruitties, key = { it.id }) { item ->
                    FruittieItem(
                        item = item,
                        onAddToCart = viewModel::addItemToCart,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                // Support edge-to-edge (required on Android 15)
                // https://developer.android.com/develop/ui/compose/layouts/insets#inset-size
                item {
                    Spacer(
                        Modifier.windowInsetsBottomHeight(
                            WindowInsets.systemBars,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun FruittieItem(
    item: Fruittie,
    onAddToCart: (fruittie: Fruittie) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.fullName,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = { onAddToCart(item) }) {
                Text(stringResource(R.string.add))
            }
        }
    }
}

@Preview
@Composable
fun ItemPreview() {
    FruittieItem(
        Fruittie(name = "Fruit", fullName = "Fruitus Mangorus", calories = "240"),
        onAddToCart = {},
    )
}
