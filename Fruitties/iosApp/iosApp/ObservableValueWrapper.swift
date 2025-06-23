import Combine
import SwiftUI
import shared

class ObservableValueWrapper<Value>: ObservableObject {

    @Published var value: Value

    init(value: Value) {
        self.value = value
    }
}
