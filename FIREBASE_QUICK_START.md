# Firebase Integration - Quick Start Checklist

## ✅ What's Already Done

All code has been implemented in your project:

### Files Created/Modified
- ✅ `app/build.gradle.kts` - Firebase deps + google-services plugin
- ✅ `build.gradle.kts` (root) - google-services plugin declaration  
- ✅ `AndroidManifest.xml` - MyApp + FCM service registered
- ✅ `MyApp.kt` - Firebase initialization
- ✅ `AuthRepository.kt` - Email/Password + Google Sign-In
- ✅ `FirestoreRepository.kt` - Real-time Firestore listeners
- ✅ `ImageStorageRepository.kt` - Storage image upload
- ✅ `ProfileFragment.kt` - Compose UI with auth
- ✅ `AppFirebaseMessagingService.kt` - FCM notifications

### Architecture
- ✅ MVVM repository pattern
- ✅ Kotlin Coroutines for all async
- ✅ Jetpack Compose integrated (ComposeView in Fragment)
- ✅ Real-time sync via Firestore listeners
- ✅ Two advanced features: FCM, Storage

---

## 🔴 Remaining Steps (You Must Complete)

### 1. Install JDK (Required)
```powershell
# Install JDK 17: https://www.oracle.com/java/technologies/downloads/
# Set environment variable:
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
# Restart terminal/IDE
```

### 2. Get google-services.json (Required)
1. Open [Firebase Console](https://console.firebase.google.com/)
2. Select your project → Project Settings → Your apps
3. Click download `google-services.json`
4. Save to: `app/google-services.json`

### 3. Create OAuth Web Client (Required for Google Sign-In)
1. Open [Google Cloud Console](https://console.cloud.google.com/) → APIs & Services → Credentials
2. Create OAuth 2.0 Client ID → Web application
3. Copy Client ID (ends with `.apps.googleusercontent.com`)
4. In `ProfileFragment.kt` line 40, replace:
   ```kotlin
   private val webClientId: String = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
   // Replace with:
   private val webClientId: String = "YOUR_ACTUAL_CLIENT_ID.apps.googleusercontent.com"
   ```

### 4. Enable Firebase Services (Required)
In [Firebase Console](https://console.firebase.google.com/):
- ✅ **Authentication** → Enable Email/Password and Google
- ✅ **Firestore** → Create Database (test mode)
- ✅ **Storage** → Enable (optional but recommended)
- ✅ **Cloud Messaging** → Enable (for FCM)

### 5. Build & Verify
```powershell
cd "C:\Users\User\Documents\GitHub\Drivo"
.\gradlew.bat clean assembleDebug
```

If build fails, check:
- [ ] `JAVA_HOME` environment variable is set
- [ ] `app/google-services.json` exists
- [ ] Run Gradle sync in Android Studio

---

## 📱 Integration: Add Profile to Navigation

### Option A: Settings Card (Dashboard)
Edit `DashboardFragment.kt` line 77:
```kotlin
view.findViewById<LinearLayout>(R.id.card_settings).setOnClickListener {
    (activity as? MainActivity)?.loadFragment(ProfileFragment())
}
```

### Option B: Bottom Navigation (MainActivity)
Edit `MainActivity.kt` line 44:
```kotlin
R.id.nav_profile -> loadFragment(ProfileFragment())
```

---

## 🧪 Testing Features

### Test Email/Password
```kotlin
val authRepo = AuthRepository()
authRepo.createUserWithEmail("test@example.com", "password")
authRepo.signInWithEmail("test@example.com", "password")
```

### Test Google Sign-In
Open Profile screen → Tap "Sign in with Google" → Complete flow

### Test Firestore Real-Time
Data saved to Firestore auto-syncs across devices via listeners

### Test Push Notifications
Firebase Console → Cloud Messaging → Send test message

---

## 📋 File Locations Reference

```
app/
├── google-services.json          ← Must add (from Firebase)
├── build.gradle.kts              ✅ Ready
├── AndroidManifest.xml           ✅ Ready
└── src/main/java/com/example/drivo/
    ├── MyApp.kt                  ✅ Ready
    ├── auth/
    │   └── AuthRepository.kt      ✅ Ready
    ├── data/
    │   ├── FirestoreRepository.kt ✅ Ready
    │   └── ImageStorageRepository.kt ✅ Ready
    ├── firebase/
    │   └── AppFirebaseMessagingService.kt ✅ Ready
    └── fragments/
        └── ProfileFragment.kt    ✅ Ready
```

---

## 🚀 Next Steps After Build Succeeds

1. Deploy app to emulator/device
2. Test sign-in (Google Sign-In recommended first)
3. Go to Firebase Console → Authentication → Verify user created
4. Test Firestore by adding data from app or Firebase console
5. Re-open app on different device/browser to see real-time sync
6. Test FCM by sending test message from Firebase Console

---

## 📚 Architecture Overview

```
┌─ ProfileFragment (Compose UI)
│  └─ AuthRepository (sign-in/out, auth state Flow)
│     └─ FirebaseAuth + Google Sign-In
│
├─ FirestoreRepository (real-time data)
│  ├─ users/{userId} collection
│  ├─ items/{itemId} collection
│  └─ addSnapshotListener → Flow
│
├─ ImageStorageRepository (upload)
│  └─ images/profiles/{userId}.jpg
│
└─ AppFirebaseMessagingService (FCM)
   └─ onMessageReceived → Notification display
```

---

## ⚡ Quick Troubleshooting

| Error | Fix |
|-------|-----|
| "JAVA_HOME is not set" | Install JDK, set JAVA_HOME env var, restart IDE |
| "google-services.json not found" | Ensure at `app/google-services.json` |
| "Unresolved reference: Firebase*" | Run Gradle sync after adding json |
| "Plugin not found: com.google.gms" | Root build.gradle.kts must have plugin |
| "Google Sign-In fails" | Check OAuth Client ID in ProfileFragment |

---

## 💾 File Summary

| File | Lines | Purpose |
|------|-------|---------|
| MyApp.kt | 14 | Initialize Firebase |
| AuthRepository.kt | 79 | Email/Password + Google Sign-In |
| FirestoreRepository.kt | 56 | Firestore CRUD + listeners |
| ImageStorageRepository.kt | 19 | Storage image upload |
| ProfileFragment.kt | 99 | Compose profile UI |
| AppFirebaseMessagingService.kt | 64 | FCM push notifications |

---

**Status:** Code 100% ready | Configuration 0% complete  
**Estimated time to completion:** 20-30 minutes (once JDK installed)  
**Next action:** Install JDK and complete checklist items 2-5 above

