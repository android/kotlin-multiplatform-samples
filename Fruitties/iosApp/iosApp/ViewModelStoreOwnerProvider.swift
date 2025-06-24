import SwiftUI
import shared

/// A SwiftUI `View` that provides a `ViewModelStoreOwner` to its content.
///
/// Manages the lifecycle of `ViewModel` instances, scoping them to this view hierarchy.
/// Clears the associated `ViewModelStore` when the provider disappears.
struct ViewModelStoreOwnerProvider<Content: View>: View {
    /// The `IOSViewModelStoreOwner` instance, wrapped for SwiftUI observability.
    @StateObject private var viewModelStoreOwner: ObservableValueWrapper<IOSViewModelStoreOwner>
    private let content: Content

    /// Initializes the provider with its content, creating a new `IOSViewModelStoreOwner`.
    init(@ViewBuilder content: () -> Content) {
        _viewModelStoreOwner = StateObject(wrappedValue: ObservableValueWrapper(value: IOSViewModelStoreOwner()))
        self.content = content()
    }

    var body: some View {
        content
            .environmentObject(viewModelStoreOwner)
            .onDisappear {
                viewModelStoreOwner.value.clear()
            }
    }
}
