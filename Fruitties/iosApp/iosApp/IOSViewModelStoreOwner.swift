import SwiftUI
import shared

/// A ViewModelStoreOwner specifically for iOS.
/// This is used with from iOS with Kotlin Multiplatform (KMP).
class IOSViewModelStoreOwner: ObservableObject, ViewModelStoreOwner {

    var viewModelStore = ViewModelStore()

    /// This function allows retrieving the androidx ViewModel from the store.
    func viewModel<T: ViewModel>(
        key: String? = nil,
        factory: ViewModelProviderFactory,
        extras: CreationExtras? = nil
    ) -> T {
        do {
            return try viewModel(
                modelClass: T.self,
                factory: factory,
                key: key,
                extras: extras
            ) as! T
        } catch {
            fatalError("Failed to create ViewModel of type \(T.self)")
        }
    }

    func clear() {
        viewModelStore.clear()
    }
}
