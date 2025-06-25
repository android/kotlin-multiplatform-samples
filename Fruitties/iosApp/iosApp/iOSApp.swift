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
    /// The application's dependency container, wrapped for SwiftUI observation.
    let appContainer: ObservableValueWrapper<AppContainer>

    init() {
        self.appContainer = ObservableValueWrapper<AppContainer>(
            value: AppContainer(factory: Factory())
        )
    }

    var body: some Scene {
        WindowGroup {
            /// Provides the root `ViewModelStoreOwner` to the environment, making it accessible to all child views.
            /// Nested `ViewModelStoreOwnerProvider` instances can create additional, scoped ViewModel stores.
            ViewModelStoreOwnerProvider {
                ContentView()
            }
            .environmentObject(appContainer)
        }
    }
}
