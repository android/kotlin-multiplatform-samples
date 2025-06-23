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
    let viewModelStoreOwnerFactory: () -> CartViewModelStoreOwner

    var body: some View {
        let viewModelStoreOwner = viewModelStoreOwnerFactory()
        let cartViewModel = viewModelStoreOwner.cartViewModel
        // The ViewModel exposes a StateFlow that we access in SwiftUI with SKIE Observing.
        // https://skie.touchlab.co/features/flows-in-swiftui
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
            .onDisappear {
                viewModelStoreOwner.clear()
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
