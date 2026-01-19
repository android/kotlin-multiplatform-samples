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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.fruitties.android.LocalAppContainer
import com.example.fruitties.android.R
import com.example.fruitties.model.Fruittie
import com.example.fruitties.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onClickViewCart: () -> Unit,
    onFruittieClick: (Fruittie) -> Unit,
    viewModel: MainViewModel = viewModel(
        factory = LocalAppContainer.current.mainViewModelFactory,
    ),
) {
    val lazyPagingItems = viewModel.fruittiesPagingData.collectAsLazyPagingItems()
    val uiState by viewModel.homeUiState.collectAsState()
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = {
                    Text(text = stringResource(R.string.frutties))
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickViewCart,
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(R.string.view_cart, uiState.cartItemCount))
                }
            }
        },
    ) { paddingValues ->
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxHeight()
                        .consumeWindowInsets(paddingValues),
                )
            }
            is LoadState.Error -> {
                ErrorStateItem(
                    message = stringResource(R.string.error_loading),
                    onRetry = { lazyPagingItems.refresh() },
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                        .padding(16.dp),
                )
            }
            else -> {
                PagingList(
                    lazyPagingItems = lazyPagingItems,
                    paddingValues = paddingValues,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                    onFruittieClick = onFruittieClick,
                    onAddToCart = viewModel::addItemToCart,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PagingList(
    lazyPagingItems: LazyPagingItems<Fruittie>,
    paddingValues: PaddingValues,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onFruittieClick: (Fruittie) -> Unit,
    onAddToCart: (Fruittie) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 72.dp),
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues),
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id },
        ) { index ->
            val item = lazyPagingItems[index]
            if (item != null) {
                FruittieItem(
                    item = item,
                    onClick = onFruittieClick,
                    onAddToCart = onAddToCart,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        item {
            when (lazyPagingItems.loadState.append) {
                is LoadState.Loading -> {
                    LoadingIndicator(modifier = Modifier.padding(16.dp))
                }
                is LoadState.Error -> {
                    ErrorStateItem(
                        message = stringResource(R.string.error_loading_more),
                        onRetry = { lazyPagingItems.retry() },
                        modifier = Modifier.padding(16.dp),
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun FruittieItem(
    item: Fruittie,
    onClick: (fruittie: Fruittie) -> Unit,
    onAddToCart: (fruittie: Fruittie) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable {
                onClick(item)
            }.padding(16.dp),
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
        onClick = {},
    )
}
