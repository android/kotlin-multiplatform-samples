import SwiftUI
import shared

/// A ViewModelStoreOwner specifically for iOS.
/// This is used with from iOS with Kotlin Multiplatform (KMP).
class IOSViewModelStoreOwner: ObservableObject, SwiftViewModelStoreOwner {

    var viewModelStore: Lifecycle_viewmodelViewModelStore =
        Lifecycle_viewmodelViewModelStore()

    /// This function allows retrieving the androidx ViewModel from the store.
    func viewModel<T: Lifecycle_viewmodelViewModel>(
        key: String? = nil,
        factory: Lifecycle_viewmodelViewModelProviderFactory,
        extras: Lifecycle_viewmodelCreationExtras? = nil
    ) -> T {
        do {
            return try viewModelStore.getViewModel(
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
