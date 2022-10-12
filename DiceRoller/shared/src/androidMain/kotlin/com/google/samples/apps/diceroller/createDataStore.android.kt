/*
 * Copyright 2022 The Android Open Source Project
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
package com.google.samples.apps.diceroller

import android.content.Context
import androidx.annotation.GuardedBy
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val Context.dataStore by DataStoreSingletonDelegate()

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)

internal class DataStoreSingletonDelegate : ReadOnlyProperty<Context, DataStore<Preferences>> {

    private val lock = Any()

    @Volatile
    @GuardedBy("lock")
    private var INSTANCE: DataStore<Preferences>? = null

    override fun getValue(thisRef: Context, property: KProperty<*>): DataStore<Preferences> {
        return INSTANCE ?: synchronized(lock) {
            if (INSTANCE == null) {
                val applicationContext = thisRef.applicationContext
                INSTANCE = createDataStore(applicationContext)
            }
            INSTANCE!!
        }
    }
}