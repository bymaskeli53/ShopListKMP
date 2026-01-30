# 🛒 ShopList

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.8.2-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-24%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-Native-000000?style=for-the-badge&logo=apple&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A modern, multilingual shopping list app built with Kotlin Multiplatform and Clean Architecture**

[Features](#-features) • [Screenshots](#-screenshots) • [Architecture](#-architecture) • [Getting Started](#-getting-started) • [Tech Stack](#️-tech-stack)

</div>

---

## 🌟 Overview

ShopList is a **Kotlin Multiplatform** shopping list application that demonstrates modern mobile development practices with **MVVM architecture**, **Material Design 3**, and **accessibility-first** design principles. Built with Compose Multiplatform, it runs natively on Android, iOS, and Desktop with 100% shared business logic and UI.

### Why ShopList?

- 🌍 **Multilingual Support**: Full localization with automatic system language detection (Turkish & English)
- 🎨 **Beautiful UI**: High-contrast design 
- ♿ **Accessibility First**: Large touch targets (48dp+), screen reader support, and TTS features
- 🔄 **Reactive Architecture**: Built with Kotlin Flow and StateFlow for seamless data updates
- 🎯 **Clean Code**: MVVM + Clean Architecture with SOLID principles
- 📱 **100% Multiplatform**: Shared codebase for Android, iOS, and Desktop
- 🗃️ **Offline First**: Local SQLDelight database with instant data persistence
- 🌙 **Dark Mode**: High-contrast dark theme optimized for readability
- 💉 **Dependency Injection**: Koin for clean, testable architecture

---

## ✨ Features

### Core Functionality

| Feature | Description | Status |
|---------|-------------|--------|
| 📝 **List Management** | Create, edit, and organize multiple shopping lists with titles | ✅ |
| 🔍 **Smart Search** | Real-time search across list titles and items | ✅ |
| 🗑️ **Swipe to Delete** | Intuitive swipe gesture with undo functionality | ✅ |
| ✅ **Completion Tracking** | Mark lists as completed with visual feedback | ✅ |
| 🔊 **Text-to-Speech** | Voice readback of shopping lists (Turkish & English) | ✅ |
| 📱 **WhatsApp Sharing** | Share shopping lists directly to WhatsApp | ✅ |
| 🎯 **Item Quantity** | Track quantities and amounts for each item | ✅ |
| ✏️ **Inline Editing** | Edit lists and items with smooth animations | ✅ |
| 🌍 **Auto Language Detection** | Automatically uses your system language | ✅ |

### Technical Highlights

- 🎭 **Smooth Animations**: Fade in/out and slide animations throughout the app
- 💾 **Instant Persistence**: All changes saved immediately to local database
- 🔄 **Reactive UI**: Automatic updates when data changes via Flow
- 🏗️ **Clean Architecture**: Proper layering with Domain, Data, and Presentation layers
- 💉 **Koin DI**: Dependency injection for testable, maintainable code
- 🧪 **Testable**: MVVM pattern with repository interfaces makes unit testing straightforward
- 📱 **Platform-Specific**: Optimized native implementations for each platform

---

## 📱 Screenshots

### Android

<div align="center">

| Main List Screen | Add List | List Details |
|:----------------:|:-------------:|:---------------:|
| ![Android Screenshot 1](docs/screenshots/android_1.png) | ![Android Screenshot 2](docs/screenshots/android_2.png) | ![Android Screenshot 3](docs/screenshots/android_3.png) |
| View all your shopping lists with smart search | Create lists with quantities | Update list detail |

</div>

### iOS

<div align="center">

| Main List Screen | Add List | List Details |
|:----------------:|:-------------:|:------------:|
| ![iOS Screenshot 1](docs/screenshots/ios_1.png) | ![iOS Screenshot 2](docs/screenshots/ios_2.png) | ![iOS Screenshot 3](docs/screenshots/ios_3.png) |
| View all your shopping lists with smart search | Create lists with quantities | Update list detail |

</div>

---

## 🏛️ Architecture

ShopList follows **Clean Architecture** principles with **MVVM** pattern and **SOLID** design:

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ ListScreen   │  │ AddScreen    │  │ DetailScreen │      │
│  │ + ViewModel  │  │ + ViewModel  │  │ + ViewModel  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│         Compose UI + Material Design 3                       │
└─────────────────────────────────────────────────────────────┘
                           ↓ ↑ StateFlow
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌────────────────────┐  ┌─────────────────────────┐        │
│  │ ShoppingRepository │  │ Domain Models           │        │
│  │    (Interface)     │  │ - ShoppingList          │        │
│  └────────────────────┘  │ - ShoppingItem          │        │
│                          │ - ShoppingItemFormState │        │
│                          └─────────────────────────┘        │
└─────────────────────────────────────────────────────────────┘
                           ↓ ↑ Kotlin Flow
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                             │
│  ┌──────────────────────────┐  ┌─────────────────────┐      │
│  │ ShoppingRepositoryImpl   │  │ Data Entities       │      │
│  │                          │  │ + Mappers           │      │
│  │ (SQLDelight Database)    │  │ (toDomain/toEntity) │      │
│  └──────────────────────────┘  └─────────────────────┘      │
└─────────────────────────────────────────────────────────────┘
                           ↓ ↑
┌─────────────────────────────────────────────────────────────┐
│                  Platform-Specific Layer                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Android    │  │     iOS      │  │   Desktop    │      │
│  │ • TTS API    │  │ • AVSpeech   │  │ • JVM TTS    │      │
│  │ • SQLite     │  │ • SQLite     │  │ • SQLite     │      │
│  │ • WhatsApp   │  │ • WhatsApp   │  │              │      │
│  │ • Material   │  │ • Native UI  │  │ • Desktop UI │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

### SOLID Principles Applied

| Principle | Implementation |
|-----------|----------------|
| **S**ingle Responsibility | Each class has one clear purpose (ViewModels manage UI state, Repository handles data) |
| **O**pen/Closed | Interfaces allow extension without modification (ShoppingRepository interface) |
| **L**iskov Substitution | ShoppingRepositoryImpl can be substituted with any implementation |
| **I**nterface Segregation | Focused interfaces (ShoppingRepository, TextToSpeechManager, ShareManager) |
| **D**ependency Inversion | High-level modules depend on abstractions, not implementations (via Koin DI) |

### Layer Responsibilities

| Layer | Responsibility | Key Components |
|-------|----------------|----------------|
| **Presentation** | UI & User Interaction | Compose screens, ViewModels, Theme, Localization |
| **Domain** | Business Logic | Repository interfaces, Domain Models |
| **Data** | Data Management | Repository implementation, Database, Mappers |
| **Platform** | Platform-Specific APIs | TTS, Database drivers, ShareManager, Theme |

---

## 🌍 Localization

ShopList features a **type-safe, Kotlin-based localization system** with automatic system language detection:

### Supported Languages

- 🇹🇷 **Turkish** (Türkçe) - Full support
- 🇬🇧 **English** - Full support

### How It Works

```kotlin
// Automatic system language detection
LocalizationManager.initialize() // Detects Turkish or English

// Access localized strings
val strings = LocalStrings.current
Text(strings.screenTitleShoppingList) // "Alışveriş Listem" or "My Shopping List"
```

### Why Kotlin-Based Instead of XML?

✅ **Type-safe** - Compile-time checking prevents typos
✅ **Multiplatform** - Works on Android, iOS, and Desktop
✅ **No Context required** - Clean Compose integration
✅ **Format functions** - Dynamic string formatting with parameters
✅ **Centralized** - Single source of truth for all platforms

---

## 🎨 Design Philosophy

### High Contrast & Accessibility

ShopList is designed with **accessibility-first** principles:

- ✅ **WCAG AA compliant** color contrast ratios (7:1 for text)
- ✅ **48dp minimum** touch targets for all interactive elements
- ✅ **Large, readable fonts** with proper typography hierarchy
- ✅ **Screen reader support** with meaningful content descriptions
- ✅ **Text-to-Speech** for hands-free list navigation
- ✅ **Smooth animations** with proper timing for cognitive accessibility
- ✅ **Search functionality** for quick list discovery
- ✅ **Swipe gestures** with undo for error prevention

### Color Palette

<div align="center">

| Color | Hex | Usage | Contrast |
|-------|-----|-------|----------|
| 🖤 **Background** | `#000000` | Pure Black - Main background | - |
| 🌑 **Surface** | `#1A1A1A` | Dark Gray - Cards and surfaces | - |
| 💚 **Accent** | `#00E676` | Bright Green - Primary actions | 7.2:1 |
| ⚪ **Text Primary** | `#FFFFFF` | White - Main text | 21:1 |
| 🔘 **Text Secondary** | `#B0B0B0` | Light Gray - Secondary text | 9.7:1 |
| 🔴 **Delete** | `#FF5252` | Red - Destructive actions | 4.8:1 |

</div>

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Ladybug | 2024.2.1 or newer
- **JDK** 17 or newer
- **Kotlin** 2.2.0
- **Gradle** 8.11.1
- **Xcode** 15+ (for iOS development)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/shoplist.git
   cd shoplist
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   ```bash
   ./gradlew build
   ```

4. **Run the app**
   - **Android**: Click "Run" or press `Shift + F10`
   - **iOS**: Open `iosApp` in Xcode and run
   - **Desktop**: `./gradlew :composeApp:run`

---

## 🔨 Building

### Android

```bash
# Debug build
./gradlew :composeApp:assembleDebug

# Release build (requires signing config)
./gradlew :composeApp:assembleRelease

# Install on connected device
./gradlew :composeApp:installDebug

# Run on emulator/device
./gradlew :composeApp:installDebug
adb shell am start -n com.gundogar.shoplist/.MainActivity
```

### iOS

```bash
# Open in Xcode
open iosApp/iosApp.xcodeproj

# Or build from command line
xcodebuild -project iosApp/iosApp.xcodeproj \
           -scheme iosApp \
           -configuration Debug
```

### Desktop (JVM)

```bash
# Run desktop application
./gradlew :composeApp:run

# Create distribution
./gradlew :composeApp:createDistributable

# Package for distribution
./gradlew :composeApp:packageDistributionForCurrentOS
```

---

## 🛠️ Tech Stack

### Core Technologies

<div align="center">

| Technology | Purpose | Version |
|:----------:|:-------:|:-------:|
| ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white) | Programming Language | 2.2.0 |
| ![Compose](https://img.shields.io/badge/Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white) | UI Framework | 1.8.2 |
| ![Coroutines](https://img.shields.io/badge/Coroutines-7F52FF?style=flat&logo=kotlin&logoColor=white) | Async Programming | 1.10.2 |
| ![Flow](https://img.shields.io/badge/Flow-7F52FF?style=flat&logo=kotlin&logoColor=white) | Reactive Streams | - |
| ![Koin](https://img.shields.io/badge/Koin-FF6F00?style=flat&logo=kotlin&logoColor=white) | Dependency Injection | 4.1.1 |

</div>

### Libraries & Dependencies

| Library | Purpose | Version |
|---------|---------|---------|
| **Material3** | UI Components & Theming | Latest |
| **Navigation Compose** | Type-safe Navigation | 2.9.0-rc01 |
| **Lifecycle ViewModel** | State Management | 2.9.1 |
| **SQLDelight** | Database ORM with Flow | 2.0.2 |
| **Kotlinx DateTime** | Date/Time handling | 0.6.1 |
| **UUID** | Unique ID generation | 0.8.4 |
| **Koin** | Dependency Injection | 4.1.1 |

### Platform-Specific Features

#### Android
- 🎤 **Android TTS API**: Native Text-to-Speech
- 📱 **WhatsApp Intent**: Direct sharing to WhatsApp
- 🗄️ **SQLDelight Android Driver**: AndroidSqliteDriver

#### iOS
- 🍎 **AVSpeechSynthesizer**: Native Text-to-Speech
- 📱 **UIActivityViewController**: Native sharing
- 🗄️ **SQLDelight Native Driver**: NativeSqliteDriver
- 🎨 **Native Theme**: iOS-style Material Design

#### Desktop (JVM)
- 🖥️ **Java TTS**: Desktop Text-to-Speech
- 🗄️ **SQLDelight JVM Driver**: SqliteDriver
- 🎨 **Desktop Theme**: Optimized for desktop

---

## 📂 Project Structure

```
ShopList/
├── composeApp/
│   └── src/
│       ├── commonMain/kotlin/com/gundogar/shoplist/
│       │   ├── domain/                    # 🎯 Business logic layer
│       │   │   ├── model/                 # Domain models
│       │   │   │   ├── ShoppingList.kt
│       │   │   │   ├── ShoppingItem.kt
│       │   │   │   └── ShoppingItemFormState.kt
│       │   │   └── repository/            # Repository interfaces
│       │   │       └── ShoppingRepository.kt
│       │   │
│       │   ├── data/                      # 💾 Data layer
│       │   │   ├── local/                 # Database drivers (expect/actual)
│       │   │   │   └── DatabaseDriverFactory.kt
│       │   │   ├── model/                 # Data entities & mappers
│       │   │   │   └── ShoppingListEntity.kt
│       │   │   └── repository/            # Repository implementations
│       │   │       └── ShoppingRepositoryImpl.kt
│       │   │
│       │   ├── presentation/              # 🎨 UI layer
│       │   │   ├── list/                  # Main list screen
│       │   │   │   ├── ShoppingListScreen.kt
│       │   │   │   ├── ShoppingListViewModel.kt
│       │   │   │   └── components/
│       │   │   │       └── ListRow.kt
│       │   │   ├── add/                   # Add list screen
│       │   │   │   ├── AddItemScreen.kt
│       │   │   │   └── AddItemViewModel.kt
│       │   │   ├── detail/                # Edit list screen
│       │   │   │   ├── DetailScreen.kt
│       │   │   │   └── DetailViewModel.kt
│       │   │   └── theme/                 # App theming (expect/actual)
│       │   │       ├── Color.kt
│       │   │       └── Theme.kt
│       │   │
│       │   ├── ui/                        # 🌍 Localization
│       │   │   └── strings/
│       │   │       ├── Strings.kt         # Interface
│       │   │       ├── TurkishStrings.kt  # Turkish impl
│       │   │       ├── EnglishStrings.kt  # English impl
│       │   │       ├── LocalizationManager.kt
│       │   │       ├── AppLanguage.kt
│       │   │       └── SystemLocale.kt    # (expect/actual)
│       │   │
│       │   ├── util/                      # 🔧 Utilities
│       │   │   ├── tts/                   # Text-to-Speech (expect/actual)
│       │   │   │   └── TextToSpeechManager.kt
│       │   │   └── share/                 # WhatsApp sharing (expect/actual)
│       │   │       └── ShareManager.kt
│       │   │
│       │   ├── di/                        # 💉 Dependency Injection
│       │   │   ├── Modules.kt
│       │   │   └── initKoin.kt
│       │   │
│       │   └── App.kt                     # Root composable & navigation
│       │
│       ├── androidMain/                   # 🤖 Android-specific code
│       │   └── kotlin/com/gundogar/shoplist/
│       │       ├── MainActivity.kt
│       │       ├── data/local/
│       │       │   └── DatabaseDriverFactory.android.kt
│       │       ├── util/
│       │       │   ├── tts/TextToSpeechManager.android.kt
│       │       │   └── share/ShareManager.android.kt
│       │       ├── presentation/theme/Theme.android.kt
│       │       ├── ui/strings/SystemLocale.android.kt
│       │       └── di/Modules.android.kt
│       │
│       ├── iosMain/                       # 🍎 iOS-specific code
│       │   └── kotlin/com/gundogar/shoplist/
│       │       ├── MainViewController.kt
│       │       ├── data/local/DatabaseDriverFactory.ios.kt
│       │       ├── util/
│       │       │   ├── tts/TextToSpeechManager.ios.kt
│       │       │   └── share/ShareManager.ios.kt
│       │       ├── presentation/theme/Theme.ios.kt
│       │       ├── ui/strings/SystemLocale.ios.kt
│       │       └── di/Modules.ios.kt
│       │
│       └── jvmMain/                       # 🖥️ Desktop-specific code
│           └── kotlin/com/gundogar/shoplist/
│               ├── main.kt
│               ├── data/local/DatabaseDriverFactory.jvm.kt
│               ├── ui/strings/SystemLocale.jvm.kt
│               └── di/Modules.jvm.kt
│
├── iosApp/                                # iOS application entry point
├── docs/                                  # Documentation & assets
│   └── screenshots/                       # App screenshots
│       ├── android_1.png
│       ├── android_2.png
│       ├── android_3.png
│       ├── ios_1.png
│       ├── ios_2.png
│       └── ios_3.png
├── CLAUDE.md                              # Development guide
└── README.md                              # This file
```

---

## 🧪 Testing

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run Android instrumentation tests
./gradlew connectedAndroidTest

# Run tests with coverage
./gradlew testDebugUnitTestCoverage

# Run all checks (tests + lint)
./gradlew check
```

### Key Testable Components

- ✅ **ViewModels**: All state transformations and business logic
- ✅ **Repository**: Database operations with Flow
- ✅ **Mappers**: Entity ↔ Domain model conversions
- ✅ **Localization**: String resource availability
- ✅ **Navigation**: Screen flow and deep links

---

## 🎯 Roadmap

### 🚧 Planned Features

- [ ] 🌐 **Cloud Sync** - Firebase/Supabase integration for multi-device sync
- [ ] 👥 **Shared Lists** - Collaborate with family members in real-time
- [ ] 📊 **Analytics** - Shopping statistics and spending insights
- [ ] 🏷️ **Categories** - Organize items by categories (Dairy, Produce, etc.)
- [ ] 📸 **Barcode Scanner** - Quick product addition via barcode
- [ ] 💰 **Price Tracking** - Monitor prices and set budget limits
- [ ] 🔔 **Smart Reminders** - Location-based and time-based notifications
- [ ] 🎨 **Theme Customization** - Custom color schemes and light mode
- [ ] 🌍 **More Languages** - Spanish, French, German, Arabic, etc.
- [ ] 📤 **Import/Export** - Backup lists to CSV/JSON
- [ ] 🔄 **Recurring Lists** - Template lists for weekly shopping
- [ ] 📝 **Notes** - Add notes to individual items

### 🎉 Recently Completed

- ✅ **Multilingual Support** (v1.4.0) - Turkish & English with auto-detection
- ✅ **WhatsApp Sharing** (v1.3.0) - Direct sharing to WhatsApp
- ✅ **Clean Variable Naming** (v1.2.0) - Refactored for clarity
- ✅ **Dependency Injection** (v1.1.0) - Koin integration
- ✅ **Text-to-Speech** (v1.0.0) - Voice readback
- ✅ **MVVM Architecture** (v1.0.0) - Clean separation of concerns

---

## 🤝 Contributing

Contributions are **welcome**! Whether it's bug fixes, new features, or documentation improvements.

### How to Contribute

1. **Fork** the repository
2. **Create** a feature branch
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Make** your changes following the code style
4. **Test** your changes thoroughly
5. **Commit** with descriptive messages
   ```bash
   git commit -m 'feat: Add some AmazingFeature'
   ```
6. **Push** to your fork
   ```bash
   git push origin feature/AmazingFeature
   ```
7. **Open** a Pull Request with a clear description

### Code Style Guidelines

- ✅ Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- ✅ Use **meaningful, descriptive** variable and function names
  - ❌ `list`, `item`, `lists`
  - ✅ `shoppingList`, `shoppingItem`, `shoppingLists`
- ✅ Write KDoc comments for public APIs
- ✅ Keep functions small and focused (< 50 lines)
- ✅ Follow **SOLID principles**
- ✅ Use **dependency injection** (Koin)
- ✅ Update documentation for API changes
- ✅ Use proper **layer separation** (Presentation → Domain → Data)

### Commit Message Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: Add new feature
fix: Fix a bug
refactor: Refactor code
docs: Update documentation
test: Add tests
chore: Maintenance tasks
```

### Areas for Contribution

- 🐛 **Bug Fixes**: Check open issues
- ✨ **New Features**: From the roadmap or your ideas
- 📝 **Documentation**: Improve guides and comments
- 🌍 **Translations**: Add support for new languages
- 🎨 **UI/UX**: Design improvements and accessibility
- 🧪 **Tests**: Increase test coverage
- 🔧 **Refactoring**: Code quality improvements

---

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 ShopList Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

---

## 👨‍💻 Author

<div align="center">

**Gundogar**

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/yourusername)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/yourprofile)
[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/yourhandle)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:your.email@example.com)

</div>



## 🔗 Useful Resources

### Official Documentation

- 📘 [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- 🎨 [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- 🎯 [Material Design 3](https://m3.material.io/)
- 💾 [SQLDelight Documentation](https://cashapp.github.io/sqldelight/)
- 💉 [Koin Documentation](https://insert-koin.io/)

### Project Documentation

- 🏗️ [Development Guide](./CLAUDE.md) - How to work with this project
- 🎨 [Architecture Decisions](./CLAUDE.md#architecture) - Why we chose this architecture
- 📋 [Contributing Guidelines](#-contributing) - How to contribute

### Learning Resources

- 🎓 [Kotlin for Android Developers](https://kotlinlang.org/docs/android-overview.html)
- 🎯 [Jetpack Compose Pathway](https://developer.android.com/courses/pathways/compose)
- 🧪 [Testing in Compose](https://developer.android.com/jetpack/compose/testing)
- 🏗️ [Clean Architecture Guide](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

## 📱 Download

<div align="center">

### Coming Soon to App Stores!

[![Google Play](https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](#)
[![App Store](https://img.shields.io/badge/App_Store-0D96F6?style=for-the-badge&logo=app-store&logoColor=white)](#)

*Currently in development. Star ⭐ the repo to get notified when it launches!*

</div>

---

## 💬 Support

Having issues or questions? We're here to help!

- 📧 **Email**: [muhammetemingundogar53@gmail.com](mailto:muhammetemingundogar53@gmail.com)

---

## 📊 Project Stats

<div align="center">

![GitHub stars](https://img.shields.io/github/stars/yourusername/shoplist?style=social)
![GitHub forks](https://img.shields.io/github/forks/yourusername/shoplist?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/yourusername/shoplist?style=social)

![Lines of Code](https://img.shields.io/tokei/lines/github/yourusername/shoplist?style=flat-square)
![GitHub code size](https://img.shields.io/github/languages/code-size/yourusername/shoplist?style=flat-square)
![GitHub repo size](https://img.shields.io/github/repo-size/yourusername/shoplist?style=flat-square)
![GitHub language count](https://img.shields.io/github/languages/count/yourusername/shoplist?style=flat-square)
![GitHub top language](https://img.shields.io/github/languages/top/yourusername/shoplist?style=flat-square&color=7F52FF)

</div>

---

<div align="center">

### ⭐ If you like this project, give it a star!

**Built with 💚 using Kotlin Multiplatform and Clean Architecture**

<sub>ShopList © 2025 - Making shopping lists beautiful, accessible, and multilingual</sub>

[⬆ Back to Top](#-shoplist)

</div>
