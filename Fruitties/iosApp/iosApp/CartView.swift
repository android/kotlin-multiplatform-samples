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
    let cart: Cart
    let dataRepository: DataRepository
    @State
    private var expanded = false

    var body: some View {
        if (cart.items.isEmpty) {
            Text("Cart is empty, add some items").padding()
        } else {
            HStack {
                Text("Cart has \(cart.items.count) items").padding()
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
                CartDetailsView(dataRepository: dataRepository)
            }
        }
    }
}

struct CartDetailsView: View {
    let dataRepository: DataRepository
    @State
    private var details: CartDetails = CartDetails(items: [])

    var body: some View {
        VStack {
            ForEach(details.items, id: \.fruittie.id) { item in
                Text("\(item.fruittie.name): \(item.count)")
            }
        }.collectWithLifecycle(dataRepository.cartDetails, binding: $details)
    }
}
