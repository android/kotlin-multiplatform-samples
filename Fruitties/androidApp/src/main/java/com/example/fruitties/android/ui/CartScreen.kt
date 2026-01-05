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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fruitties.android.LocalAppContainer
import com.example.fruitties.android.R
import com.example.fruitties.model.CartItemDetails
import com.example.fruitties.model.Fruittie
import com.example.fruitties.viewmodel.CartUiState
import com.example.fruitties.viewmodel.CartViewModel

@Composable
fun CartScreen(
    onNavBarBack: () -> Unit,
    viewModel: CartViewModel = viewModel(factory = LocalAppContainer.current.cartViewModelFactory),
) {
    val cartState by viewModel.cartUiState.collectAsState()

    CartScreen(
        onNavBarBack = onNavBarBack,
        cartState = cartState,
        increaseCountClick = viewModel::increaseCountClick,
        decreaseCountClick = viewModel::decreaseCountClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavBarBack: () -> Unit,
    cartState: CartUiState,
    decreaseCountClick: (CartItemDetails) -> Unit,
    increaseCountClick: (CartItemDetails) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavBarBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_24px),
                            contentDescription = stringResource(R.string.navigate_back),
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
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(16.dp),
        ) {
            val cartItemCount = cartState.totalItemCount
            Text(
                text = stringResource(R.string.cart_has_items, cartItemCount),
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(cartState.cartDetails) { cartItem ->
                    CartItem(
                        cartItem = cartItem,
                        decreaseCountClick = decreaseCountClick,
                        increaseCountClick = increaseCountClick,
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
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "${cartItem.count}x")
        Spacer(Modifier.width(8.dp))
        Text(text = cartItem.fruittie.name)
        Spacer(Modifier.weight(1f))
        FilledIconButton(
            onClick = { decreaseCountClick(cartItem) },
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.error),
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

@Preview
@Composable
private fun CartScreenPreview() {
    FruittiesTheme {
        CartScreen(
            onNavBarBack = {},
            cartState = CartUiState(
                cartDetails = listOf(
                    CartItemDetails(
                        fruittie = Fruittie(
                            name = "Banana",
                            fullName = "Banana Banana",
                            calories = "100",
                        ),
                        count = 4,
                    ),
                    CartItemDetails(
                        fruittie = Fruittie(
                            name = "Orange",
                            fullName = "Orange Orange",
                            calories = "100",
                        ),
                        count = 1,
                    ),
                    CartItemDetails(
                        fruittie = Fruittie(
                            name = "Apple",
                            fullName = "Apple Apple",
                            calories = "100",
                        ),
                        count = 100,
                    ),
                ),
            ),
            decreaseCountClick = {},
            increaseCountClick = {},
        )
    }
}

@Preview
@Composable
private fun CartItemPreview() {
    FruittiesTheme {
        CartItem(
            cartItem = CartItemDetails(
                fruittie = Fruittie(
                    name = "Banana",
                    fullName = "Banana Banana",
                    calories = "100",
                ),
                count = 4,
            ),
            increaseCountClick = {},
            decreaseCountClick = {},
        )
    }
}
