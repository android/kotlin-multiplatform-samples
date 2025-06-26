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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fruitties.android.R
import com.example.fruitties.android.di.App
import com.example.fruitties.model.CartItemDetails
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.creationExtras

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(onNavBarBack: () -> Unit) {
    // Instantiate a ViewModel with a dependency on the AppContainer.
    // To make ViewModel compatible with KMP, the ViewModel factory must
    // create an instance without referencing the Android Application.
    // Here we put the KMP-compatible AppContainer into the extras
    // so it can be passed to the ViewModel factory.
    val app = LocalContext.current.applicationContext as App

    val viewModel: CartViewModel = viewModel(
        factory = CartViewModel.Factory,
        extras = creationExtras(app.container),
    )

    val cartState by viewModel.cartUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavBarBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.cart))
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
                // Support edge-to-edge (required on Android 15)
                // https://developer.android.com/develop/ui/compose/layouts/insets#inset-size
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            val cartItemCount = cartState.totalItemCount
            Text(
                text = "Cart has $cartItemCount items",
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(cartState.cartDetails) { cartItem ->
                    CartItem(
                        cartItem = cartItem,
                        decreaseCountClick = viewModel::decreaseCountClick,
                        increaseCountClick = viewModel::increaseCountClick,
                    )
                }
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
fun CartItem(
    cartItem: CartItemDetails,
    increaseCountClick: (CartItemDetails) -> Unit,
    decreaseCountClick: (CartItemDetails) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "${cartItem.count}x")
        Spacer(Modifier.width(8.dp))
        Text(text = cartItem.fruittie.name)
        Spacer(Modifier.weight(1f))
        FilledIconButton(
            onClick = { decreaseCountClick(cartItem) },
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Red),
        ) {
            Text(
                text = "-",
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
            )
        }
        FilledIconButton(
            onClick = { increaseCountClick(cartItem) },
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Green),
        ) {
            Text(
                text = "+",
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
            )
        }
    }
}
