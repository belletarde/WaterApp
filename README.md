# Take Water - Water Tracker & Hydration Reminder 💧

A modern Android water tracking application built with Jetpack Compose, following Clean Architecture and SOLID principles.

## 📱 Features

- 📊 Visual water tracking with animated glass
- 🎯 Personalized daily goals based on weight and activity level
- ⏰ Smart hydration reminders
- 📈 Weekly and hourly consumption reports
- 🎨 Beautiful Material Design 3 UI
- 💾 Privacy-first: all data stored locally
- 🔔 Customizable notification schedule

## 🏗️ Architecture

This project follows Clean Architecture principles with clear separation of concerns:

### Layers

- **Domain Layer**: Business logic and use cases
- **Data Layer**: Repositories, DAOs, and data sources
- **Presentation Layer**: MVVM with Jetpack Compose

### Tech Stack

- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Koin
- **Database**: Room with Flow
- **Async**: Kotlin Coroutines & Flow
- **Testing**: JUnit, MockK, Coroutines Test

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 11 or higher
- Android SDK 28+ (Android 9.0+)
- Gradle 8.5.0+

### Setup

1. Clone the repository
```bash
git clone https://github.com/yourusername/take-water.git
cd take-water
```

2. Open in Android Studio

3. Sync Gradle dependencies

4. Run the app on an emulator or physical device

### Building Release APK

1. Copy `local.properties.example` to `local.properties`
```bash
cp local.properties.example local.properties
```

2. Generate a keystore (only once)
```bash
keytool -genkey -v -keystore release-keystore.jks \
  -alias take-water-key -keyalg RSA -keysize 2048 -validity 10000
```

3. Update `local.properties` with your keystore information
```properties
KEYSTORE_FILE=release-keystore.jks
KEYSTORE_PASSWORD=your_password
KEY_ALIAS=take-water-key
KEY_PASSWORD=your_key_password
```

4. Build release AAB
```bash
./gradlew bundleRelease
```

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## 🌍 Supported Languages

- English
- Portuguese (Brazil)
- Spanish
- Hindi
- Turkish
- Indonesian
- Russian
- Urdu

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📞 Contact

For questions or feedback, please open an issue on GitHub.

---

Made with ❤️ using Kotlin and Jetpack Compose
