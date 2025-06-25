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

import Foundation
import SwiftUI
import shared

struct CartView : View {
    /// Injects the `IOSViewModelStoreOwner` from the environment, which manages the lifecycle of `ViewModel` instances.
    @EnvironmentObject var viewModelStoreOwner: ObservableValueWrapper<IOSViewModelStoreOwner>

    /// Injects the `AppContainer` from the environment, providing access to application-wide dependencies.
    @EnvironmentObject var appContainer: ObservableValueWrapper<AppContainer>

    var body: some View {
        /// Retrieves the `CartViewModel` instance using the `viewModelStoreOwner`.
        /// The `CartViewModel.Factory` and `creationExtras` are provided to enable dependency
        /// injection and proper initialization of the ViewModel with its required `AppContainer`.
        let cartViewModelFromEnum: CartViewModel = viewModelStoreOwner.value.getViewModelWithEnum(
            type: .cart,
            factory: CartViewModel.companion.Factory,
            extras: CartViewModel.companion.creationExtras(appContainer: appContainer.value)
        ) as! CartViewModel
        let cartViewModelWithFunctionUsingEnum: CartViewModel = viewModelStoreOwner.value.getCartViewModelWithFunctionUsingEnum(
            factory: CartViewModel.companion.Factory,
            extras: CartViewModel.companion.creationExtras(appContainer: appContainer.value)
        )
        let cartViewModelWithFunctionUsingProvider: CartViewModel = viewModelStoreOwner.value.getCartViewModelWithFunctionUsingProvider(
            factory: CartViewModel.companion.Factory,
            extras: CartViewModel.companion.creationExtras(appContainer: appContainer.value)
        )
        // Any of the 3 options work.
//        let cartViewModel = cartViewModelFromEnum
//        let cartViewModel = cartViewModelWithFunctionUsingEnum
        let cartViewModel = cartViewModelWithFunctionUsingProvider

        // https://skie.touchlab.co/features/flows-in-swiftui
        Observing(cartViewModel.cartUiState) { cartUIState in
            VStack {
                HStack {
                    let total = cartUIState.totalItemCount
                    Text("Cart has \(total) items").padding()
                    Spacer()
                }
                CartDetailsView(cartViewModel: cartViewModel)
                Spacer()
            }
        }
    }
}

struct CartDetailsView: View {
    let cartViewModel: CartViewModel

    var body: some View {
        // https://skie.touchlab.co/features/flows-in-swiftui
        Observing(self.cartViewModel.cartUiState) { cartUIState in
            ScrollView {
                LazyVStack {
                    ForEach(cartUIState.cartDetails, id: \.fruittie.id) { item in
                        Text("\(item.fruittie.name): \(item.count)")
                    }
                }
            }
        }
    }
}
