package com.example.fruitties.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fruitties.di.AppContainer

val APP_CONTAINER_KEY = CreationExtras.Key<AppContainer>()

fun creationExtras(appContainer: AppContainer): CreationExtras =
    MutableCreationExtras().apply {
        set(APP_CONTAINER_KEY, appContainer)
    }

fun creationExtras(
    appContainer: AppContainer,
    additional: MutableCreationExtras.() -> Unit,
): CreationExtras =
    MutableCreationExtras().apply {
        set(APP_CONTAINER_KEY, appContainer)
        additional()
    }

inline fun <reified T : ViewModel> fruittiesViewModelFactory(crossinline initializer: CreationExtras.(AppContainer) -> T) =
    viewModelFactory {
        initializer {
            val appContainer = this[APP_CONTAINER_KEY] as AppContainer
            this.initializer(appContainer)
        }
    }
