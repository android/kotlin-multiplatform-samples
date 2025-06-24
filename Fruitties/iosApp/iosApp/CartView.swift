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
        /// The `CartViewModel.Factory` and `creationExtras` are provided to enable dependency injection
        /// and proper initialization of the ViewModel with its required `AppContainer`.
//        let cartViewModel: CartViewModel = viewModelStoreOwner.value.getViewModel(
//            type: .cart,
//            factory: CartViewModel.companion.Factory,
//            extras: CartViewModel.companion.creationExtras(appContainer: appContainer.value)
//        ) as! CartViewModel
        let cartViewModel: CartViewModel = viewModelStoreOwner.value.getCartViewModel(
            factory: CartViewModel.companion.Factory,
            extras: CartViewModel.companion.creationExtras(appContainer: appContainer.value)
        )

        /// Observes the `cartUiState` `StateFlow` from the `CartViewModel` using SKIE's `Observing` utility.
        /// This allows SwiftUI to react to changes in the cart's UI state.
        /// For more details, refer to: https://skie.touchlab.co/features/flows-in-swiftui
        Observing(cartViewModel.cartUiState) { cartUIState in
            VStack {
                HStack {
                    let total = cartUIState.cartDetails.reduce(0) { $0 + $1.count }
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
        /// Observes the `cartUiState` `StateFlow` from the `CartViewModel` using SKIE's `Observing` utility.
        /// This allows SwiftUI to react to changes in the cart's UI state.
        /// For more details, refer to: https://skie.touchlab.co/features/flows-in-swiftui
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
