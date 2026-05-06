# 🧠 AutoMind: The Intelligent Productivity Assistant

AutoMind is a cutting-edge, AI-powered productivity engine for Android designed to reclaim your time. Unlike traditional app blockers, AutoMind **learns your behavior**, **predicts distraction patterns**, and **autonomously intervenes** to keep you in the zone.

<img width="1693" height="929" alt="image" src="https://github.com/user-attachments/assets/e6d97dad-a55e-4352-b720-6ef65f69b9b4" />

[**Explore the Repository »**](https://github.com/TopGnextdoor/AutoMind)

---

## 🌟 Vision
In an era of infinite scrolls and notifications, AutoMind acts as a digital guardian. It doesn't just restrict; it **educates** and **adapts** to your personal focus journey.

---

## 🚀 Game-Changing Features

### 🤖 Autopilot Mode (AI Enforcement)
*   **Adaptive Discipline Score**: The app monitors your focus consistency. A higher score means more trust; a lower score triggers **Aggressive Mode**.
*   **Context-Aware Blocking**: Autopilot automatically restricts apps during your "Danger Zones" (hours when you're most likely to procrastinate).
*   **Dynamic Rule Adjustment**: No more manual setup. The system adjusts its strictness based on your real-time behavior.

### 📊 Advanced Analytics & Visualization
*   **High-Fidelity Charts**: Custom Canvas-based visualizations for daily usage patterns.
*   **Focus Heatmaps**: Identify your peak productivity windows and distraction triggers.
*   **Top Distractions breakdown**: Precise data on where your minutes go.
*   **Weekly Trend Tracking**: Visualize your improvement over time with smooth, animated line graphs.

### 💡 Smart Suggestion Engine
*   **Habit Learning**: AutoMind identifies recurring distractions.
*   **Actionable Cards**: Get suggestions like *"You spend 40 mins on YouTube every Tuesday at 10 PM. Create a focus rule?"*
*   **One-Tap Automation**: Turn insights into rules with a single click.

### ☁️ Seamless Cloud Architecture
*   **Real-time Firebase Sync**: Your rules and preferences are synced across all your Android devices.
*   **Offline-First Stability**: Local Room database ensures the app works perfectly in the mountains or in the subway.

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3) |
| **Logic Engine** | Kotlin Coroutines & Flow |
| **Local Storage** | Room Database & Jetpack DataStore |
| **Cloud Backend** | Firebase Auth & Cloud Firestore |
| **Design System** | Futuristic Cyber-Punk / Glassmorphism |
| **Monitoring** | UsageStatsManager & Foreground Services |

---

## 🚦 Getting Started

### 📋 Prerequisites
*   Android 8.0 (API level 26) or higher.
*   Android Studio Ladybug or newer.
*   A valid `google-services.json` file in the `app/` directory.

### 🔑 Critical Permissions
To function effectively, AutoMind requires:
1.  **Usage Access**: To detect which apps are currently active.
2.  **Display Over Other Apps**: To show focus overlays and hard-blocks.
3.  **Notification Access**: For smart interventions.

### 🔨 Installation
1.  **Clone the Repo**:
    ```bash
    git clone https://github.com/TopGnextdoor/AutoMind.git
    ```
2.  **Firebase Setup**: Add your `google-services.json` to the `app/` folder.
3.  **Build & Run**: Use Android Studio to deploy to your physical device.

---

## 🎨 Design System
AutoMind uses a curated, high-contrast palette designed for focus and premium feel:
*   **Deep Space Background**: `#0F0F12`
*   **Electric Blue (Primary)**: `#00D2FF`
*   **Vivid Purple (Secondary)**: `#9D50BB`
*   **Glassy Surfaces**: Semi-transparent layers with 16dp-24dp corner radii.

---

## 🛡 Stability & Performance
*   **Smart Sleep**: Background polling stops when the screen is off to maximize battery life.
*   **Crash-Resilient**: Global error handling ensures no data loss during sync issues.
*   **Lightweight**: Optimized to use minimal RAM even during deep pattern analysis.

---
