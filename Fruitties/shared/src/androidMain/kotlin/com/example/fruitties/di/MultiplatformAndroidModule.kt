/*
 * Copyright 2025 The Android Open Source Project
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

package com.example.fruitties.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.fruitties.database.AppDatabase
import com.example.fruitties.database.CartDataStore
import com.example.fruitties.database.DB_FILE_NAME
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.FruittieViewModel
import com.example.fruitties.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val multiplatformModule: Module = module {
    single {
        val app = androidApplication()
        val dbFile = app.getDatabasePath(DB_FILE_NAME)
        Room
            .databaseBuilder<AppDatabase>(
                context = app,
                name = dbFile.absolutePath,
            ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single {
        val app = androidApplication()
        CartDataStore {
            app.filesDir
                .resolve(
                    "cart.json",
                ).absolutePath
        }
    }

    viewModelOf(::CartViewModel)
    viewModelOf(::MainViewModel)
    viewModel { parameters ->
        FruittieViewModel(
            fruittieId = parameters.get(),
            repository = get()
        )
    }
}
