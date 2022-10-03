/*
 * Copyright 2022 The Android Open Source Project
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

import Combine
import SwiftUI

struct ContentView: View {
    @StateObject var viewModel: SettingsViewModel = SettingsViewModel()

    var body: some View {
        NavigationView {
            GeometryReader { geo in
                VStack {
                    GameView().frame(height: geo.size.height * 0.5)
                    Divider()
                    SettingsView().frame(height: geo.size.height * 0.5)
                }
            }.task {
                await viewModel.startObservingSettings()
            }.environmentObject(viewModel)
            .navigationTitle("screen_title")
        }
    }
}
