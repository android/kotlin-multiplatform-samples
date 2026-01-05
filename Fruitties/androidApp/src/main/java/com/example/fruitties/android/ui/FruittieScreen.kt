package com.example.fruitties.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fruitties.android.LocalAppContainer
import com.example.fruitties.android.R
import com.example.fruitties.model.Fruittie
import com.example.fruitties.viewmodel.FruittieViewModel
import com.example.fruitties.viewmodel.FruittieViewModel.Companion.FRUITTIE_ID_KEY
import com.example.fruitties.viewmodel.creationExtras

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruittieScreen(
    fruittieId: Long,
    onNavBarBack: () -> Unit,
    viewModel: FruittieViewModel = viewModel(
        key = "fruittie_$fruittieId",
        factory = LocalAppContainer.current.fruittieViewModelFactory,
        extras = creationExtras {
            set(FRUITTIE_ID_KEY, fruittieId)
        },
    ),
) {
    val state = viewModel.state.collectAsState().value

    FruittieScreen(
        state = state,
        onNavBarBack = onNavBarBack,
        addToCart = { viewModel.addToCart(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruittieScreen(
    state: FruittieViewModel.State,
    addToCart: (Fruittie) -> Unit,
    onNavBarBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        (state as? FruittieViewModel.State.Content)?.fruittie?.name
                            ?: stringResource(R.string.loading),
                    )
                },
                actions = {
                    val inCart = (state as? FruittieViewModel.State.Content)?.inCart ?: 0
                    Text(stringResource(R.string.in_cart, inCart))
                    Spacer(Modifier.width(8.dp))
                },
                navigationIcon = {
                    IconButton(onClick = onNavBarBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_24px),
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            // Check if state is loaded
            if (state !is FruittieViewModel.State.Content) return@Scaffold

            FloatingActionButton(
                shape = MaterialTheme.shapes.extraLarge,
                onClick = { addToCart(state.fruittie) },
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_shopping_cart_24px),
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(R.string.add_to_cart))
                }
            }
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            when (state) {
                FruittieViewModel.State.Loading -> CircularProgressIndicator()
                is FruittieViewModel.State.Content -> {
                    Text(state.fruittie.fullName)
                    Text(state.fruittie.calories)
                }
            }
        }
    }
}
