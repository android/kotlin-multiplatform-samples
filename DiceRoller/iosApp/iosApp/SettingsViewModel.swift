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

import shared
import Combine
import KMPNativeCoroutinesAsync

@MainActor
final class SettingsViewModel: ObservableObject {
    private let repository = DiceSettingsRepository(dataStore: CreateDataStoreKt.createDataStore())
    private var roller = DiceRoller()

    @Published
    var diceCount: Int = Int(DiceSettingsRepository.companion.DEFAULT_DICE_COUNT)

    @Published
    var sideCount: Int = Int(DiceSettingsRepository.companion.DEFAULT_SIDES_COUNT)

    @Published
    var uniqueRollsOnly: Bool = DiceSettingsRepository.companion.DEFAULT_UNIQUE_ROLLS_ONLY

    @Published
    var rollButtonLabel: String? = nil

    @Published
    private (set) var rollResultLabel: String? = nil

    var isSettingsModified: Bool {
        Int32(diceCount) != currentSettings?.diceCount ||
        Int32(sideCount) != currentSettings?.sideCount ||
        uniqueRollsOnly != currentSettings?.uniqueRollsOnly
    }

    private var currentSettings: DiceSettings? = nil

    func startObservingSettings() async {
        do {
            let stream = asyncSequence(for: repository.settings)
            for try await settings in stream {
                self.diceCount = Int(settings.diceCount)
                self.sideCount = Int(settings.sideCount)
                self.uniqueRollsOnly = settings.uniqueRollsOnly
                self.rollButtonLabel = String.localizedStringWithFormat(NSLocalizedString("game_roll_button", comment: ""), settings.diceCount, settings.sideCount)
                self.currentSettings = settings
            }
        } catch {
            print("Failed with error: \(error)")
        }
    }

    func saveSettings() {
        repository.saveSettings(diceCount: Int32(diceCount), sideCount: Int32(sideCount), uniqueRollsOnly: uniqueRollsOnly)
    }

    func rollDice() {
        guard let settings = currentSettings else {
            return
        }

        do {
            let rolledNumbers: [KotlinInt] = try roller.rollDice(settings: settings)
            rollResultLabel = String(
                format: NSLocalizedString("game_result_success", comment: ""),
                rolledNumbers.map(String.init).joined(separator: ", ")
            )
        } catch {
            rollResultLabel = NSLocalizedString("game_result_error", comment: "")
        }
    }
}
