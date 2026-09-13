# Json Launcher 🚀 `{ }`

<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Json Launcher Logo" width="120" height="120" />
</p>

<p align="center">
  <b>Your Android home screen, serialized.</b><br>
  A minimalist, developer-first Android home screen launcher built natively with <b>Kotlin</b> and <b>Jetpack Compose</b> that renders your mobile environment as an interactive, live, syntax-highlighted JSON document.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%207.0%2B%20(API%2024--36)-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Kotlin%202.2.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose%20%2B%20Material%203-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/DI-Dagger%20Hilt%202.57-brightgreen?style=flat-square" alt="Hilt" />
  <img src="https://img.shields.io/badge/Font-JetBrains%20Mono-black?style=flat-square" alt="JetBrains Mono" />
  <img src="https://img.shields.io/badge/Version-1.6.2-blue?style=flat-square" alt="Version" />
</p>

---

## 📑 Overview

Most Android launchers replicate desktop-style grids, widget arrangements, and icon packs. **Json Launcher** rethinks the mobile interface entirely through the lens of a software engineer: **what if your entire phone was a live JSON object?**

Everything on your device—live time, battery statistics, active notifications, pinned applications, application shortcuts, and settings—is parsed, formatted, and displayed using IDE-style syntax highlighting and monospace typography.

```json
{
  "system": {
    "time": "19:05:17",
    "date": "Sunday, 13 Sep 2026",
    "battery": 88,
    "LauncherSettings": "launch"
  },
  "notifications": [
    {
      "WhatsApp": {
        "title": "Awais",
        "text": "Check out the new Json Launcher build!"
      }
    }
  ],
  "apps": {
    "Phone": {
      "packageName": "com.google.android.dialer",
      "Info": "launch"
    },
    "Messages": {
      "packageName": "com.google.android.apps.messaging",
      "Info": "launch",
      "Start chat": "launch"
    }
  }
}
/* Swipe Left for App Drawer */
```

---

## ✨ Features

### 🖥️ 1. Interactive JSON Home Screen
- **Live System Node**:
  - Updates time (`HH:mm:ss`), formatted date, and real-time battery percentage every second.
  - Direct executable action `"LauncherSettings": launch` to jump straight to launcher configuration.
- **Collapsible Nodes (`>` / `⌄`)**:
  - Every object (`system`, `notifications`, `apps`) and nested item can be folded (`..`) or expanded on click.
- **Real-Time Notification Stream**:
  - Intercepts active status-bar notifications through Android's `NotificationListenerService`.
  - Automatically formats incoming alerts by app name with `"title"` and `"text"` properties.
  - Filters out persistent/ongoing background events and immediately cleans up dismissed notifications.
- **Direct App Launch & Deep Links**:
  - Single tap on any app key launches the application.
  - Expanding an app reveals its `"packageName"`, an `"Info"` action (opens Android Application Details Settings), and native Android **App Shortcuts** (dynamic, manifest, or pinned shortcuts like *"New Tab"* or *"New Note"*).

### 🔍 2. Terminal-Style App Drawer (`sys.find(_)`)
- **Swipe-to-Drawer Transition**:
  - Smooth horizontal paging powered by `HorizontalPager` with custom scale and alpha parallax depth transitions.
- **Code-Style Search**:
  - Integrated command-style search bar formatted as `sys.find("<query>")`.
  - Instant fuzzy search across all installed applications.
  - Hardware back gesture resets the search filter instantly.
- **Kinetic Alphabet Slider**:
  - Interactive vertical A–Z fast-scroller with top quick-jump indicator (`◉`).
  - Spring-physics animations (`Spring.DampingRatioNoBouncy`) with dynamic letter scaling up to `2.0x`.
  - Floating preview bubble indicator with smooth spring translation.
  - Tactile haptic feedback (`HapticFeedbackConstants.CLOCK_TICK`) on every letter tick.
- **Live Package Observation**:
  - Broadcast receiver automatically refreshes the drawer whenever applications are installed, updated, or removed.

### ⚙️ 3. Developer-First Settings Screen
- Rendered completely in JSON syntax (`"settings": { ... }`):
  - **`"default Launcher"`**: Request the default home launcher role via Android's `RoleManager` (API 29+) or fallback intent.
  - **`"home apps"`**: Manage pinned favorites directly. Remove apps with `⨯` or expand `"add": { ... }` with an inline `sys.find(_)` query and `+` button to pin apps to the home screen.
  - **`"color theme"`**: Toggle themes using JSON boolean values (`"light"`, `"dark"`, `"system"`). Clicking `true` / `false` switches themes instantly.
  - **`"background"`**:
    - `"default"`: Clean, distraction-free solid surface.
    - `"wallpaper"`: Semi-transparent backdrop (`FLAG_SHOW_WALLPAPER`) allowing your system wallpaper to show through beneath the code.
    - `"pick_wallpaper": launch`: Fast trigger to open the system wallpaper chooser.

### 🎨 4. IDE-Accurate Theme & Typography
- **JetBrains Mono Typography**: Native bundling of `JetBrainsMono-Regular`, `Medium`, `SemiBold`, and `Bold` across all headings, keys, values, and comments.
- **Tailored Syntax Palettes**:
  - **Dark Theme (VS Code Inspired)**:
    - Keys: `#4FC1FF` (Blue)
    - Strings: `#CE9178` (Orange)
    - Numbers: `#B5CEA8` (Soft Green)
    - Booleans: `#C586C0` (Purple)
    - Comments: `#6A9955` (Muted Green)
    - Brackets / Parentheses: `#FFD700` (Gold)
  - **Light Theme (GitHub Inspired)**:
    - Keys: `#005CC5` (Blue)
    - Strings: `#032F62` (Deep Navy)
    - Numbers: `#22863A` (Green)
    - Booleans: `#6F42C1` (Purple)
    - Comments: `#6A737D` (Slate Gray)
    - Brackets / Parentheses: `#B08800` (Amber Gold)

---

## 🏗️ Architecture & Technology Stack

Json Launcher follows modern Android architecture principles with **MVVM (Model-View-ViewModel)**, **Unidirectional Data Flow (UDF)**, and clean separation of concerns.

```
                  ┌───────────────────────────────┐
                  │      Jetpack Compose UI       │
                  │ (HomeScreen, Drawer, Settings)│
                  └──────────────┬────────────────┘
                                 │ Observes UI State (StateFlow)
                                 ▼
                  ┌───────────────────────────────┐
                  │          ViewModels           │
                  │  (StateFlow, CoroutineScopes) │
                  └──────────────┬────────────────┘
                                 │ Calls Domain / Use-cases
                                 ▼
                  ┌───────────────────────────────┐
                  │         Repositories          │
                  │ (AppsRepo, NotifRepo, Config) │
                  └───────┬──────────────┬────────┘
                          │              │
        ┌─────────────────▼──┐        ┌──▼───────────────────┐
        │  Android Services  │        │ Preferences DataStore│
        │  - LauncherApps    │        │  (Pinned apps,       │
        │  - NotifListener   │        │   Theme, Wallpaper)  │
        │  - BroadcastRecv   │        └──────────────────────┘
        └────────────────────┘
```

| Layer | Technologies / Libraries |
| :--- | :--- |
| **Language** | [Kotlin 2.2.10](https://kotlinlang.org/) |
| **UI Toolkit** | [Jetpack Compose (BOM 2024.09.00)](https://developer.android.com/jetpack/compose) + Material 3 |
| **Navigation** | AndroidX Navigation Compose `2.9.3` with slide & fade transitions |
| **Dependency Injection** | [Dagger Hilt 2.57](https://dagger.dev/hilt/) with KSP (`2.3.6`) |
| **Persistence** | Jetpack [Preferences DataStore 1.1.7](https://developer.android.com/topic/libraries/architecture/datastore) |
| **System Services** | `LauncherApps`, `ShortcutManager`, `RoleManager`, `NotificationListenerService` |
| **Typography** | [JetBrains Mono](https://www.jetbrains.com/lp/mono/) |
| **Build Tools** | Android Gradle Plugin `9.3.1`, Min SDK `24`, Target/Compile SDK `36` (Android 16) |

---

## 📁 Project Structure

```
JsonLauncher/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml
│   │       ├── java/com/awais/jsonlauncher/
│   │       │   ├── JsonLauncherApp.kt          # Application class, Hilt entry point & service monitoring
│   │       │   ├── MainActivity.kt             # Launcher Activity, Edge-to-Edge & Role handling
│   │       │   ├── SetJsonLauncherTheme.kt     # Global theme provider & scaffold
│   │       │   │
│   │       │   ├── listeners/
│   │       │   │   └── JsonNotificationListener.kt  # NotificationListenerService implementation
│   │       │   │
│   │       │   ├── models/
│   │       │   │   ├── AppInfo.kt              # App item representation with shortcuts
│   │       │   │   ├── AppShortcut.kt          # Android dynamic/manifest shortcut model
│   │       │   │   ├── BackgroundMode.kt       # DEFAULT (Solid) vs WALLPAPER (Transparent)
│   │       │   │   ├── JsonProperty.kt         # Key-value JSON node representation
│   │       │   │   ├── NotificationInfo.kt     # Parsed notification data model
│   │       │   │   └── ThemeMode.kt            # LIGHT, DARK, SYSTEM
│   │       │   │
│   │       │   ├── navigation/
│   │       │   │   └── JsonLauncherNavHost.kt  # HorizontalPager (Home & Drawer) + Settings route
│   │       │   │
│   │       │   ├── receivers/
│   │       │   │   └── PackageChangeReceiver.kt # BroadcastReceiver for app install/remove events
│   │       │   │
│   │       │   ├── repositories/
│   │       │   │   ├── AppsRepository.kt       # Queries LauncherApps, shortcuts & package status
│   │       │   │   ├── NotificationRepository.kt # Reactive notification flow cache
│   │       │   │   └── SettingsRepository.kt   # Preferences DataStore manager
│   │       │   │
│   │       │   └── ui/
│   │       │       ├── components/
│   │       │       │   ├── SearchBar.kt        # Terminal-style `sys.find(_)` input
│   │       │       │   └── jsonObject/
│   │       │       │       └── JsonItem.kt     # Core syntax highlighter & node renderer
│   │       │       ├── dialogs/
│   │       │       │   ├── NotificationAccessDialog.kt # Permission prompt dialog
│   │       │       │   └── SetAsDefaultDialog.kt       # Default launcher prompt dialog
│   │       │       ├── screens/
│   │       │       │   ├── appDrawer/          # Drawer list, AlphabetSlider & ViewModel
│   │       │       │   ├── home/               # Home screen, SystemSection, NotificationsSection, AppsSection
│   │       │       │   └── settings/           # Settings sub-screens (ColorTheme, HomeApps, Background, DefaultLauncher)
│   │       │       └── theme/
│   │       │           ├── Color.kt            # Syntax highlighting & Material 3 color definitions
│   │       │           ├── Dimension.kt        # JSON indentation & spacing constants
│   │       │           ├── SyntaxColor.kt      # CompositionLocal syntax color provider
│   │       │           ├── Theme.kt            # Main JsonLauncherTheme composable
│   │       │           └── Type.kt             # JetBrains Mono typography hierarchy
│   │       │
│   │       └── res/
│   │           ├── font/                       # JetBrains Mono TTF font family files
│   │           ├── mipmap-*/                   # Launcher icons & Play Store icon
│   │           └── values/                     # Colors, Strings & Themes
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml                      # Version catalog
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🔒 Permissions & Security

JsonLauncher requires minimal permissions to function as a fully featured home screen:

| Permission / Role | Purpose |
| :--- | :--- |
| `android.permission.VIBRATE` | Provides haptic tick feedback while sliding through the alphabet fast-scroller. |
| `android.permission.BIND_NOTIFICATION_LISTENER_SERVICE` | Required to read and display your notifications inside the JSON notifications array. |
| `android.permission.POST_NOTIFICATIONS` | Runtime permission on Android 13+ (API 33+) for notification handling. |
| `android.app.role.ROLE_HOME` | System home role to serve as your default launcher. |

> **Privacy Guarantee**: Json Launcher requires **zero internet permissions** (`android.permission.INTERNET` is not requested). All your notifications, installed applications, and preferences remain strictly on your device.

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug (2024.2.1) or newer
- **JDK**: Java 11 or higher
- **Android SDK**: Compile SDK `36`, Min SDK `24`

### Building from Source

1. **Clone the repository:**
   ```bash
   git clone https://github.com/sandhu02/json-launcher.git
   cd json-launcher
   ```

2. **Open the project** in Android Studio and let Gradle sync.

3. **Assemble the debug build:**
   ```bash
   # On macOS / Linux:
   ./gradlew assembleDebug

   # On Windows:
   .\gradlew.bat assembleDebug
   ```

4. **Install directly to a connected Android device or emulator:**
   ```bash
   ./gradlew installDebug
   ```

---

## 💡 Usage & Tips

- **First Launch**: Upon starting, grant **Notification Access** when prompted to enable live notification parsing.
- **Set as Default Launcher**: Go into `"settings"` ➔ `"default Launcher"` and tap `launch` under `"switch"`, or respond to the initial setup dialog.
- **Quick-Access App Info**: Expand any app on the home screen or app drawer, and tap `launch` on `"Info"` to immediately jump to the Android App Info settings screen.
- **Direct Shortcuts**: Expand apps like WhatsApp, YouTube, or Chrome to access deep app shortcuts like *"New Tab"* or *"New Chat"* directly from your home screen.
- **Show Wallpaper**: In `"settings"` ➔ `"background"`, set `"wallpaper": true` to see your wallpaper behind your JSON code.

---

## 🤝 Contributing

Contributions, feature suggestions, and bug reports are welcome!
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 👤 Author

- **Awais** - GitHub: [@sandhu02](https://github.com/sandhu02)

---

<p align="center">
  <sub>Crafted with ❤️ for developers and minimalism enthusiasts.</sub>
</p>
