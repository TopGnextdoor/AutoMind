# 🧠 AutoMind: Sync Your Focus

AutoMind is a futuristic, intelligent productivity assistant for Android. It doesn't just block apps; it **learns** your habits, **identifies** your distraction cycles, and **autonomously** protects your time using advanced adaptive logic.

![AutoMind Hero](https://via.placeholder.com/1200x600/0F0F12/00D2FF?text=AutoMind+-+Intelligent+Focus)

---

## 🚀 Key Features

### 🤖 Autopilot Mode
The heart of AutoMind. Autopilot takes control of your device's strictness based on your **Discipline Score**.
- **Adaptive Strictness**: As your focus improves, the system relaxes. If you slip up, it enters "Aggressive Mode" to force-block distractions.
- **Dynamic Blocking**: Automatically detects and blocks distracting apps during your personalized "Danger Zones," even without manual rules.

### 📊 Advanced Analytics & Insights
Transform your usage data into actionable intelligence with high-fidelity, animated visualizations.
- **Focus Score**: A real-time metric of your digital discipline.
- **Usage Heatmaps**: Visualize your activity distribution across 24 hours.
- **Top Distractions**: Identify exactly which apps are stealing your time.
- **Danger Zones**: AI-detected hours where you are most vulnerable to distractions.

### 💡 Smart Suggestions
AutoMind analyzes your usage patterns in the background to suggest new focus rules.
- **Habit Detection**: "You use Instagram daily at 11 PM. Add a focus rule?"
- **One-Tap Acceptance**: Convert habits into automation rules instantly.

### ☁️ Cloud Sync & Privacy
- **Firebase Integration**: Sync your rules and preferences across all your devices securely.
- **Offline-First**: AutoMind works perfectly without an internet connection, syncing only when you're back online.
- **Privacy-First**: All granular usage monitoring stays on your device.

---

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 & Custom Canvas Visualizations.
- **Architecture**: MVVM with Clean Architecture principles.
- **Local Database**: [Room](https://developer.android.com/training/data-storage/room) for usage history and rules.
- **Preferences**: [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) for reactive settings.
- **Backend**: Firebase Auth & Firestore.
- **Concurrency**: Kotlin Coroutines & Flow.

---

## 📦 Project Structure

```text
com.automind/
├── data/           # Repository pattern & Local/Remote data sources
│   ├── local/      # Room DB, DAOs, DataStore
│   ├── remote/     # Firestore integration
│   └── model/      # Unified data entities
├── logic/          # The "Brain" (Engines, Analyzers, Trackers)
│   ├── AutopilotManager.kt
│   ├── AnalyticsEngine.kt
│   └── SuggestionEngine.kt
├── ui/             # Modern, futuristic Compose UI
│   ├── screens/    # Dashboard, Autopilot, Insights, etc.
│   ├── components/ # Reusable Glassy components & Charts
│   └── theme/      # Dark-mode first design system
└── service/        # Background focus monitoring
```

---

## 🚦 Getting Started

### Prerequisites
- Android Studio Iguana or newer.
- A Firebase project (add `google-services.json` to the `app/` folder).

### Permissions
AutoMind requires two critical permissions to function:
1. **Usage Access**: To monitor which apps are currently in the foreground.
2. **System Overlay**: To show focus warnings and blocking screens.

### Installation
1. Clone the repository.
2. Build the project in Android Studio.
3. Grant permissions via the guided onboarding flow.

---

## 🎨 Design Philosophy
AutoMind features a **Futuristic Dark-Mode** aesthetic inspired by cyber-punk and glassmorphism.
- **Primary Color**: `#00D2FF` (Electric Blue)
- **Secondary Color**: `#9D50BB` (Vivid Purple)
- **Background**: `#0F0F12` (Deep Space)

---

## 🛡 Stability & Performance
- **Smart Sleep**: Monitoring pauses when the screen is off to conserve battery.
- **Thread Safety**: All I/O operations are offloaded to `Dispatchers.IO` using Coroutines.
- **Error Resilient**: Robust `try-catch` wrapping for all database and network operations.

---

Developed with ❤️ by the AutoMind Team.
