import Combine
import SwiftUI
import shared

/// A generic wrapper that makes any `Value` type observable by SwiftUI.
///
/// Use this to wrap non-`ObservableObject` types when their changes need to update SwiftUI views.
class ObservableValueWrapper<Value>: ObservableObject {

    /// The wrapped value. Changes trigger SwiftUI view updates.
    @Published var value: Value

    /// Initializes the wrapper with an initial value.
    init(value: Value) {
        self.value = value
    }
}
