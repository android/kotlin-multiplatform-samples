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

import SwiftUI
import shared

/// A SwiftUI `View` that provides a `ViewModelStoreOwner` to its content.
///
/// Manages the lifecycle of `ViewModel` instances, scoping them to this view hierarchy.
/// Clears the associated `ViewModelStore` when the provider disappears.
struct ViewModelStoreOwnerProvider<Content: View>: View {
    /// The `IOSViewModelStoreOwner` instance, wrapped for SwiftUI observability.
    @StateObject private var viewModelStoreOwner: ObservableValueWrapper<IOSViewModelStoreOwner>
    private let content: Content

    /// Initializes the provider with its content, creating a new `IOSViewModelStoreOwner`.
    init(@ViewBuilder content: () -> Content) {
        _viewModelStoreOwner = StateObject(wrappedValue: ObservableValueWrapper(value: IOSViewModelStoreOwner()))
        self.content = content()
    }

    var body: some View {
        content
            .environmentObject(viewModelStoreOwner)
            .onDisappear {
                viewModelStoreOwner.value.clear()
            }
    }
}
