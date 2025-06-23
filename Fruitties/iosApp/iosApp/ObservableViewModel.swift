import Foundation
import shared

class ObservableViewModel: ObservableObject {
    let viewModel: MainViewModel

    init(viewModel: MainViewModel) {
        self.viewModel = viewModel
    }
}
