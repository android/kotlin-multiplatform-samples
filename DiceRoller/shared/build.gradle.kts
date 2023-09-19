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
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.skie)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

version = "1.0"

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidLibrary {
        @Suppress("UnstableApiUsage")
        namespace = "com.google.samples.apps.diceroller.shared"
        @Suppress("UnstableApiUsage")
        compileSdk = 34
    }

    ios()
    iosSimulatorArm64()

    cocoapods {
        summary = "Dice Roller data module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(libs.androidx.datastore.preferences.core)
                api(libs.androidx.datastore.core.okio)
                implementation(libs.kotlinx.atomicfu)
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }

}
