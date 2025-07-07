import SwiftUI
import shared

/// A SwiftUI `View` that provides a `ViewModelStoreOwner` to its content.
///
/// Manages the lifecycle of `ViewModel` instances, scoping them to this view hierarchy.
/// Clears the associated `ViewModelStore` when the provider disappears.
struct ViewModelStoreOwnerProvider<Content: View>: View {
    @StateObject private var viewModelStoreOwner = IOSViewModelStoreOwner()
    
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


