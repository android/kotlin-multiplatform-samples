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
import shared
import Combine

class SwiftViewModelStoreOwner: NSObject, IOSViewModelStoreOwner, ObservableObject {
    // Conforms to ViewModelStoreOwner
    let viewModelStore: IOSViewModelStore

    override init() {
        self.viewModelStore = createIOSViewModelStore()
        super.init()
    }

    func clear() {
        viewModelStore.store.clear()
    }

    func getMainViewModel(
        factory: IOSViewModelProviderFactory,
        extras: IOSCreationExtras,
    ) -> MainViewModel {
        return createViewModelProvider(
            owner: self,
            factory: factory,
            extras: extras
        ).get(modelClass: MainViewModel.companion.KClass) as! MainViewModel
    }

    func getCartViewModel(
        factory: IOSViewModelProviderFactory,
        extras: IOSCreationExtras,
    ) -> CartViewModel {
        return createViewModelProvider(
            owner: self,
            factory: factory,
            extras: extras
        ).get(modelClass: CartViewModel.companion.KClass) as! CartViewModel
    }
}
