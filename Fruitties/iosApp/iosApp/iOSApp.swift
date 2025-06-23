/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 20.0 (the "License");
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

import SwiftUI
import shared

@main
struct iOSApp: App {
    // We define extras to be passed to the ViewModelStoreOwner so that it can instantiate ViewModel instances.
    let extras: IOSViewModelStoreOwner.Extras

    init() {
        self.extras = IOSViewModelStoreOwner.Extras(appContainer: AppContainer(factory: Factory()))
    }

    var body: some Scene {
        WindowGroup {
            // Provide the root ViewModelStoreOwner.
            // This will provide an @EnvironmentObject that views can use to find the nearest ViewModelStoreOwner.
            // Nested parts of the app can create additional ViewModelStoreOwner instances
            // by nesting declarations of ViewModelStoreOwnerProvider().
            ViewModelStoreOwnerProvider(extras: self.extras) {
                ContentView()
            }
        }
    }
}
