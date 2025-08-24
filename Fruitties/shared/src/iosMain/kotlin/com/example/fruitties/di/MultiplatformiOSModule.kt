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
import com.example.fruitties.database.AppDatabase
import com.example.fruitties.database.DB_FILE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.fruitties.database.CartDataStore
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.FruittieViewModel
import com.example.fruitties.viewmodel.MainViewModel
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.dsl.factoryOf
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual val multiplatformModule: Module = module {
    single {
        val dbFile = "${fileDirectory()}/$DB_FILE_NAME"
        Room
            .databaseBuilder<AppDatabase>(
                name = dbFile,
            ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single {
        CartDataStore {
            "${fileDirectory()}/cart.json"
        }
    }

    factoryOf(::MainViewModel)
    factoryOf(::CartViewModel)
    factory {parameters ->
        FruittieViewModel(fruittieId = parameters.get(), repository = get())
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory).path!!
}
