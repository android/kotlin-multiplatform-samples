import SwiftUI
import shared

/// A SwiftUI `View` that provides a `ViewModelStoreOwner` to its content.
///
/// Manages the lifecycle of `ViewModel` instances, scoping them to this view hierarchy.
/// Clears the associated `ViewModelStore` when the provider disappears.
struct ViewModelStoreOwnerProvider<Content: View>: View {
    @StateObject private var viewModelStoreOwner: IOSViewModelStoreOwner =
        IOSViewModelStoreOwner()
    
    private let content: Content

    /// Initializes the provider with its content, creating a new `IOSViewModelStoreOwner`.
    init(@ViewBuilder content: () -> Content) {

        self.content = content()
    }

    var body: some View {
        content
            .environmentObject(viewModelStoreOwner)
            .onDisappear {
                viewModelStoreOwner.clear()
            }
    }
}

/// A ViewModelStoreOwner specifically for iOS.
/// This is used with from iOS with Kotlin Multiplatform (KMP).
class IOSViewModelStoreOwner: ObservableObject {

    var viewModelStore: Lifecycle_viewmodelViewModelStore =
        Lifecycle_viewmodelViewModelStore()

    func viewModel<T: AnyObject>(
        key: String? = nil,
        factory: Lifecycle_viewmodelViewModelProviderFactory,
        extras: Lifecycle_viewmodelCreationExtras,
    ) -> T {
        let vm =
            viewModelStore.getViewModel(
                modelClass: T.self,
                factory: factory,
                key: key,
                extras: extras
            ) as! T

        return vm
    }

    func clear() {
        viewModelStore.clear()
    }
}
