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

struct ContentView: View {
    /// Injects the `IOSViewModelStoreOwner` from the environment, which manages the lifecycle of `ViewModel` instances.
    @EnvironmentObject var viewModelStoreOwner: IOSViewModelStoreOwner

    /// Injects the `AppContainer` from the environment, providing access to application-wide dependencies.
    @EnvironmentObject var appContainer: ObservableValueWrapper<AppContainer>

    var body: some View {
        /// Retrieves the `MainViewModel` instance using the `viewModelStoreOwner`.
        /// The `MainViewModel.Factory` and `creationExtras` are provided to enable dependency injection
        /// and proper initialization of the ViewModel with its required `AppContainer`.
        let mainViewModel: MainViewModel = viewModelStoreOwner.viewModel(
            factory: appContainer.value.mainViewModelFactory
        )
        NavigationStack {
            Observing(mainViewModel.homeUiState) { homeUIState in
                List {
                    ForEach(homeUIState.fruitties, id: \.self) {
                        value in
                        HStack {
                            NavigationLink {
                                FruittieScreen(fruittie: value)
                            } label: {
                                FruittieView(fruittie: value)
                            }
                            Spacer()
                            Button(
                                "Add",
                                action: {
                                    mainViewModel.addItemToCart(
                                        fruittie: value
                                    )
                                }
                            ).buttonStyle(.bordered)
                        }
                    }
                }
            }
            .navigationTitle("Fruitties")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    NavigationLink {
                        CartView()
                    } label: {
                        Observing(mainViewModel.homeUiState) { homeUIState in
                            let total = homeUIState.cartItemCount
                            Text("View Cart (\(total))")
                        }
                    }
                }
            }
        }
    }
}

struct FruittieView: View {
    var fruittie: Fruittie
    var body: some View {
        VStack {
            Text("\(fruittie.name)")
                .fontWeight(.bold)
                .frame(maxWidth: .infinity, alignment: .leading)
            Text("\(fruittie.fullName)")
                .frame(maxWidth: .infinity, alignment: .leading)
        }.padding(.vertical, 5)
    }
}
