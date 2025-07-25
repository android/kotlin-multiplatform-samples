# Fruitties
Fruitties is a sample app using Kotlin Multiplatform built for Android with Jetpack Compose and iOS with SwiftUI.

<img width="45%" alt="iOS screenshot" src="https://github.com/user-attachments/assets/0bbbe6cc-c992-40c8-8759-08689ff0a900" />
<img width="45%" alt="Android  screenshot" src="https://github.com/user-attachments/assets/8e0af83a-204f-4d16-874b-79a4bc871c43" />

## Multiplatform Jetpack Libraries 
This project integrates several Jetpack libraries that are KMP compatible:

- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Room](https://developer.android.com/kotlin/multiplatform/room)
- [DataStore](https://developer.android.com/kotlin/multiplatform/datastore)

## Running The app

We recommend installing the [Kotlin Multiplatform Plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform) to Android Studio.
This plugin offers several features to ease the development experience:

-   **New project wizard**: Create a new multiplatform project within the IDE.
-   **Preflight checks**: Preflight checks help you configure your environment.
-   **Run configurations**: Run, debug, and test applications on both iOS and
    Android directly from the IDE.
-   **Basic Swift support in the IDE**: Get basic Swift support in the IDE,
    including cross-language debugging tools, navigation and quick
    documentation.

### Android

1. Open the `Fruitties` project in Android Studio
2. Select the `androidApp` run configuration and run the app <img width="557" height="122" alt="image" src="https://github.com/user-attachments/assets/54019a6a-6877-421a-b195-ddfaf7e4a721" />

### iOS

With the [Kotlin Multiplatform Plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform) installed, you can run the app from Android Studio.

1. Open the `Fruitties` project in Android Studio
2. Select the `iosApp` run configuration <img width="623" height="103" alt="image" src="https://github.com/user-attachments/assets/56ff4eb7-f45a-4043-8aca-9733cb59d053" />


#### Run with Xcode
Alternatively to running from Android Studio, you can open this project in Xcode and run it from there:

1. Locate the `iosApp` within the `Fruitties` folder and open it with Finder <img width="762" height="579" alt="image" src="https://github.com/user-attachments/assets/3bdc2dd3-bb01-47b1-81c0-e8e1c0f43991" />
2. Open the `iosApp.xcodeproj` <img width="192" height="103" alt="image" src="https://github.com/user-attachments/assets/a933eb69-7d71-44b5-aff0-adcf7931f2db" />
3. Build and run the iOS app from Xcode by clicking the Run icon <img width="243" height="42" alt="image" src="https://github.com/user-attachments/assets/a278e57a-7d13-4299-94a8-01ffd6e9e89f" />



## License

```
Copyright 2025 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
