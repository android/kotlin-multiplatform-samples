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
    let mainViewModel: MainViewModel

    @State
    private var expanded = false

    var body: some View {
        // https://skie.touchlab.co/features/flows-in-swiftui
        Observing(self.mainViewModel.cartUiState) { cartUIState in
            VStack {
                HStack {
                    let total = cartUIState.cartDetails.reduce(0) { $0 + $1.count }
                    Text("Cart has \(total) items").padding()
                    Spacer()
                    Button {
                        expanded.toggle()
                    } label: {
                        if (expanded) {
                            Text("collapse")
                        } else {
                            Text("expand")
                        }
                    }.padding()
                }
                if (expanded) {
                    CartDetailsView(mainViewModel: mainViewModel)
                }
            }
        }
    }
}

struct CartDetailsView: View {
    let mainViewModel: MainViewModel

    var body: some View {
        
        Observing(self.mainViewModel.cartUiState) { cartUIState in
            VStack {
                ForEach(cartUIState.cartDetails, id: \.fruittie.id) { item in
                    Text("\(item.fruittie.name): \(item.count)")
                }
            }
        }
    }
}
