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

package com.example.fruitties.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fruitties.di.AppContainer
import com.example.fruitties.di.Factory

actual fun createMainViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = (this[APPLICATION_KEY])

        // TODO(cartland): How do I get the _existing_ AppContainer from (App : Application)?
        // This creates a duplicate AppContainer -- not what I want!
        val repository = AppContainer(Factory(application!!)).dataRepository
        MainViewModel(repository = repository)

        // I could try to cast the Application as App, but then the `shared` module would depend on
        // the `androidApp` module -- this would be wrong.
        // val application = (this[APPLICATION_KEY] as App)
    }
}
