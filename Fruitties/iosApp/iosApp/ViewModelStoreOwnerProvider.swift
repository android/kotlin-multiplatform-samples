import SwiftUI
import shared

struct ViewModelStoreOwnerProvider<Content: View>: View {
    @StateObject private var viewModelStoreOwner: ObservableValueWrapper<FruittiesViewModelStoreOwner>
    private let content: Content

    init(extras: FruittiesViewModelStoreOwner.Extras, @ViewBuilder content: () -> Content) {
        _viewModelStoreOwner = StateObject(wrappedValue: ObservableValueWrapper(value: FruittiesViewModelStoreOwner(extras: extras)))
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
