import SwiftUI
import shared

struct ViewModelStoreOwnerProvider<Content: View>: View {
    @StateObject private var viewModelStoreOwner: ObservableValueWrapper<IOSViewModelStoreOwner>
    private let content: Content

    init(extras: IOSViewModelStoreOwner.Extras, @ViewBuilder content: () -> Content) {
        _viewModelStoreOwner = StateObject(wrappedValue: ObservableValueWrapper(value: IOSViewModelStoreOwner(extras: extras)))
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
