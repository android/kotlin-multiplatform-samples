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

struct FruittieScreen: View {
    @StateObject var viewModelStoreOwner = IOSViewModelStoreOwner()
    @EnvironmentObject var appContainer: ObservableValueWrapper<AppContainer>
    let fruittie: Fruittie

    var body: some View {
        let fruittieViewModel: FruittieViewModel =
            viewModelStoreOwner.viewModel(
                key: "fruittie_\(fruittie.id)",
                factory: appContainer.value.fruittieViewModelFactory,
                extras: creationExtras { extras in
                    extras.set(
                        key: FruittieViewModel.companion.FRUITTIE_ID_KEY,
                        t: fruittie.id
                    )
                }
            )

        Observing(fruittieViewModel.state) { uiState in
            switch onEnum(of: uiState) {
            case .loading:
                VStack {
                    ProgressView()
                    Text("Loading fruittie details...")
                }
                .navigationTitle(fruittie.name)

            case .content(let content):
                VStack {
                    Text(content.fruittie.fullName)
                        .font(.title2)
                        .padding(.bottom, 5)
                    Text("\(content.fruittie.calories)")
                        .font(.title3)

                    Spacer()
                    Button(action: {
                        fruittieViewModel.addToCart(fruittie: content.fruittie)
                    }) {
                        HStack {
                            Image(systemName: "cart.fill.badge.plus")
                            Text("Add to cart")
                        }
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                    }
                    .padding(.horizontal)
                }
                .navigationTitle(content.fruittie.name)
                .toolbar {
                    ToolbarItem(placement: .navigationBarTrailing) {
                        Text("In cart: \(content.inCart)")
                    }
                }
            }

        }
    }
}
