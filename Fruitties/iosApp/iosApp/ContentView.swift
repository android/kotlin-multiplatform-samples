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
import Foundation

struct ContentView: View {
    @ObservedObject var uiModel: UIModel
    init(appContainer: AppContainer) {
        self.uiModel = UIModel(dataRepository: appContainer.dataRepository)
    }

    var body: some View {
        Text("Fruitties").font(.largeTitle).fontWeight(.bold)
        CartView(cart: uiModel.cart, dataRepository: uiModel.dataRepository)
        ScrollView {
            LazyVStack {
                ForEach(uiModel.fruitties, id: \.self) { value in
                    FruittieView(fruittie: value, addToCart: { fruittie in
                        Task {
                            await uiModel.addToCart(fruittie: fruittie)
                        }
                    })
                }
            }
        }.task {
            await uiModel.activate()
        }
    }
}

struct FruittieView: View {
    var fruittie: Fruittie
    var addToCart: (Fruittie) -> Void
    var body: some View {
        HStack(alignment: .firstTextBaseline) {
            ZStack {
                RoundedRectangle(cornerRadius: 15).fill(Color(red: 0.8, green: 0.8, blue: 1.0))
                VStack {
                    Text("\(fruittie.name)")
                        .fontWeight(.bold)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text("\(fruittie.fullName)")
                        .frame(maxWidth: .infinity, alignment: .leading)
                }.padding()
                Spacer()
                Button(action: { addToCart(fruittie) }, label: {
                    Text("Add")
                }).padding().frame(maxWidth: .infinity, alignment: .trailing)
            }.padding([.leading, .trailing])
        }
    }
}

class UIModel: ObservableObject {
    let dataRepository : DataRepository
    init(dataRepository: DataRepository) {
        self.dataRepository = dataRepository
    }
    @Published
    private(set) var fruitties: [Fruittie] = []
    @Published
    private(set) var cart: Cart = Cart(items: [])

    @MainActor
    func observeDatabase() async {
        for await fruitties in dataRepository.getData() {
            self.fruitties = fruitties
        }
    }
    
    @MainActor
    func watchCart() async {
        for await cart in dataRepository.getCart() {
            self.cart = cart
        }
    }
    
    func addToCart(fruittie: Fruittie) async {
        try? await dataRepository.addToCart(fruittie: fruittie)
    }

    @MainActor
    func activate() async {
        async let db: () = observeDatabase()
        async let cartUpdate: () = watchCart()
        await (db, cartUpdate)
    }
}
