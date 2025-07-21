import SwiftUI
import shared

/// A ViewModelStoreOwner specifically for iOS to be an ObservableObject.
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

    /// This can be called from outside when using the `ViewModelStoreOwnerProvider`
    func clear() {
        viewModelStore.clear()
    }

    /// This is called when this class is used as a `@StateObject`
    deinit {
        viewModelStore.clear()
    }
}
