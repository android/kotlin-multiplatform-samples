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

struct CartView: View {
    /// Injects the `IOSViewModelStoreOwner` from the environment, which manages the lifecycle of `ViewModel` instances.
    @StateObject var viewModelStoreOwner = IOSViewModelStoreOwner()

    /// Injects the `AppContainer` from the environment, providing access to application-wide dependencies.
    @EnvironmentObject var appContainer: ObservableValueWrapper<AppContainer>

    var body: some View {
        /// Retrieves the `CartViewModel` instance using the `viewModelStoreOwner`.
        /// The `CartViewModel.Factory` and `creationExtras` are provided to enable dependency injection
        /// and proper initialization of the ViewModel with its required `AppContainer`.
        let cartViewModel: CartViewModel = viewModelStoreOwner.viewModel(
            factory: appContainer.value.cartViewModelFactory
        )

        /// Observes the `cartUiState` `StateFlow` from the `CartViewModel` using SKIE's `Observing` utility.
        /// This allows SwiftUI to react to changes in the cart's UI state.
        /// For more details, refer to: https://skie.touchlab.co/features/flows-in-swiftui
        Observing(cartViewModel.cartUiState) { cartUIState in
            VStack {
                CartDetailsView(cartViewModel: cartViewModel)
                Spacer()
            }
            .navigationTitle("Cart")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Observing(cartViewModel.cartUiState) { cartUIState in
                        let total = cartUIState.totalItemCount
                        Text("Cart has \(total) items")
                    }
                }
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
            List {
                ForEach(cartUIState.cartDetails, id: \.fruittie.id) { item in
                    HStack {
                        Text("\(item.count)x \(item.fruittie.name)")
                            .frame(maxWidth: .infinity, alignment: .leading)
                        Spacer()
                        Button(action: {
                            self.cartViewModel.decreaseCountClick(
                                cartItem: item
                            )
                        }) {
                            Image(systemName: "minus.circle.fill")
                                .foregroundColor(.red)
                        }
                        .buttonStyle(.plain)

                        Button(action: {
                            self.cartViewModel.increaseCountClick(
                                cartItem: item
                            )
                        }) {
                            Image(systemName: "plus.circle.fill")
                                .foregroundColor(.green)
                        }
                        .buttonStyle(.plain)
                    }
                }
            }
            .listStyle(.inset)
            .animation(.default, value: cartUIState.cartDetails)
        }
    }
}
