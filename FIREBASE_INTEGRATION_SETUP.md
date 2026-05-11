# Firebase Integration Setup Guide (Assignment #05)

## Overview
This document provides complete setup instructions to finish the Firebase integration upgrade to your Drivo Android project. All code files have been created and Gradle build files have been configured. You need to complete a few manual steps to make the project fully functional.

## Completed Implementation

### ✅ Code Changes Applied

**Modified Files:**
- `app/build.gradle.kts` - Added Firebase BOM, dependencies (Auth, Firestore, Storage, Messaging), Google Sign-In, and google-services plugin
- `build.gradle.kts` (root) - Added google-services plugin declaration
- `app/src/main/AndroidManifest.xml` - Registered `MyApp` Application class and `AppFirebaseMessagingService`

**New Files Created:**

1. **Application Class** (`app/src/main/java/com/example/drivo/MyApp.kt`)
   - Initializes Firebase on app startup
   
2. **Authentication Layer** (`app/src/main/java/com/example/drivo/auth/AuthRepository.kt`)
   - Email/Password authentication
   - Google Sign-In integration
   - Auth state Flow for reactive UI updates
   - Coroutine-friendly suspend functions

3. **Firestore Data Layer** (`app/src/main/java/com/example/drivo/data/FirestoreRepository.kt`)
   - Real-time listeners using `addSnapshotListener` wrapped in Flows
   - Two collections: `users` and `items`
   - Multi-device synchronization support
   - CRUD operations (create, read, delete)

4. **Firebase Storage Helper** (`app/src/main/java/com/example/drivo/data/ImageStorageRepository.kt`)
   - Profile image upload functionality
   - Returns public download URL after upload

5. **Jetpack Compose Profile Screen** (`app/src/main/java/com/example/drivo/fragments/ProfileFragment.kt`)
   - Fragment hosting a ComposeView (hybrid approach)
   - Sign-in/Sign-out UI using Compose
   - Google Sign-In button and flow
   - Demonstrates Compose integration in Fragment-based app

6. **Push Notifications Service** (`app/src/main/java/com/example/drivo/firebase/AppFirebaseMessagingService.kt`)
   - Firebase Cloud Messaging (FCM) integration
   - Token callback for server-side messaging
   - Simple notification display

### ✅ Dependencies Added
```
Firebase:
- firebase-auth-ktx
- firebase-firestore-ktx
- firebase-storage-ktx
- firebase-messaging-ktx

Google Services:
- play-services-auth (Google Sign-In)

Coroutines:
- kotlinx-coroutines-play-services (for Task.await())
```

## Remaining Manual Setup Steps

### 1. Install Java Development Kit (JDK)

The environment must have Java 11+ installed and JAVA_HOME environment variable set.

**Option A: Install JDK**
- Download JDK 17 from [oracle.com](https://www.oracle.com/java/technologies/downloads/) or [openjdk.org](https://openjdk.org/)
- Install to default location (e.g., `C:\Program Files\Java\jdk-17`)
- Set environment variable:
  ```powershell
  # In PowerShell (as Administrator):
  [Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
  # Restart terminal/IDE after setting
  ```

**Option B: Use Android Studio's Embedded JDK**
If you have Android Studio:
```powershell
# Find Android Studio's JDK path (usually):
# C:\Users\[User]\AppData\Local\Android\Sdk\jdk\17.0.x

$env:JAVA_HOME = "C:\Users\User\AppData\Local\Android\Sdk\jdk\17.0.0"
```

### 2. Download and Add google-services.json

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project (or create one if you haven't)
3. Go to **Project Settings** → **Your apps** → Android app
4. If no Android app exists, click **+ Add app** and select **Add Firebase to your Android app**
5. Download the `google-services.json` file
6. Place it in: `app/google-services.json` (same directory as app/build.gradle.kts)

**Verify placement:**
```
C:\Users\User\Documents\GitHub\Drivo\
├── app/
│   ├── google-services.json  ← Place file here
│   ├── build.gradle.kts
│   └── src/
```

### 3. Configure Google Sign-In OAuth Client ID

1. In [Firebase Console](https://console.firebase.google.com/):
   - Go to **Project Settings** → **Service Accounts**
   - Copy your project ID

2. Go to [Google Cloud Console](https://console.cloud.google.com/) → APIs & Services → Credentials
3. Create a new OAuth 2.0 Client ID:
   - Application type: **Web application**
   - Name: "Drivo Web Client"
   - Authorized JavaScript origins: `https://localhost` (for testing)
4. Copy the **Client ID** (ends with `.apps.googleusercontent.com`)

5. In `ProfileFragment.kt`, replace the placeholder:
   ```kotlin
   // BEFORE:
   private val webClientId: String = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
   
   // AFTER (example):
   private val webClientId: String = "123456789-abc1d2e3f4g5h6.apps.googleusercontent.com"
   ```

### 4. Enable Authentication Methods in Firebase

1. In [Firebase Console](https://console.firebase.google.com/):
   - Go to **Build** → **Authentication**
   - Click **Set up sign-in method** (or **Add new provider**)
   - Enable **Email/Password**
   - Enable **Google**
   - Save

### 5. Create Firestore Database

1. In [Firebase Console](https://console.firebase.google.com/):
   - Go to **Build** → **Firestore Database**
   - Click **Create Database**
   - Select **Start in test mode** (for development)
   - Choose your region
   - Create

**Suggested Firestore Collections Structure:**
```
users/{userId}/
  - displayName: string
  - email: string
  - avatarUrl: string
  - fcmToken: string
  - createdAt: timestamp

items/{itemId}/
  - ownerId: string (userId reference)
  - title: string
  - description: string
  - updatedAt: timestamp
```

### 6. Enable Cloud Storage (Optional but Recommended)

1. In [Firebase Console](https://console.firebase.google.com/):
   - Go to **Build** → **Storage**
   - Click **Get started**
   - Start in test mode
   - Choose your region

### 7. Enable Cloud Messaging (Optional)

1. In [Firebase Console](https://console.firebase.google.com/):
   - Go to **Engage** → **Messaging**
   - Click **Create your first campaign** (if prompted)
   - Enable the service

## Build and Run Commands

Once JAVA_HOME is set and google-services.json is in place:

```powershell
# Navigate to project
cd "C:\Users\User\Documents\GitHub\Drivo"

# Build debug APK
.\gradlew.bat clean assembleDebug

# Or run tests
.\gradlew.bat test

# Clean Gradle cache if needed
.\gradlew.bat clean
```

## Testing Each Feature

### Test Email/Password Authentication
```kotlin
// In any Fragment/Activity with access to AuthRepository:
val authRepo = AuthRepository()

// Create user
val result = authRepo.createUserWithEmail("user@test.com", "password123")

// Sign in
val loginResult = authRepo.signInWithEmail("user@test.com", "password123")
```

### Test Google Sign-In
1. Open the app and navigate to the Profile screen (Settings card → Profile)
2. Tap "Sign in with Google"
3. Complete Google sign-in flow
4. Verify user appears in Firebase Console → Authentication

### Test Firestore Real-Time Sync
```kotlin
// In your Fragment
val authState = authRepo.observeAuthState().collectLatest { state ->
    when (state) {
        is AuthState.SignedIn -> {
            val items = firestoreRepo.observeItemsForUser(state.uid)
                .collect { snapshots ->
                    snapshots.forEach { doc ->
                        Log.d("Firestore", "Item: ${doc.id}")
                    }
                }
        }
        else -> { /* not signed in */ }
    }
}
```

### Test Push Notifications
1. Build and run the app
2. In Firebase Console → Cloud Messaging:
   - Click "Send your first message"
   - Enter title and message
   - Select "Send test message"
   - Select your device
   - Send
3. You should see a notification appear on your device/emulator

### Test Firebase Storage Image Upload
```kotlin
// In your Fragment
val imageStorageRepo = ImageStorageRepository()

// After user selects an image (using file picker)
lifecycleScope.launch {
    try {
        val downloadUrl = imageStorageRepo.uploadProfileImage(userId, imageUri)
        // Save downloadUrl to Firestore users/{userId}.avatarUrl
        firestoreRepo.createOrUpdateUser(userId, mapOf("avatarUrl" to downloadUrl))
    } catch (e: Exception) {
        Log.e("Upload", "Failed: ${e.message}")
    }
}
```

## Integration: Navigating to ProfileFragment

To add the new Profile screen to your app navigation:

**Option 1: Add Settings Card Click Handler**

In `DashboardFragment.setupCardClickListeners()`:
```kotlin
// Replace the settings card listener with:
view.findViewById<LinearLayout>(R.id.card_settings).setOnClickListener {
    (activity as? MainActivity)?.loadFragment(ProfileFragment())
}
```

**Option 2: Add Bottom Navigation Item**

In `MainActivity.setupBottomNavigation()`:
```kotlin
R.id.nav_profile -> loadFragment(ProfileFragment())
```

## Project Architecture Summary

### MVVM/Repository Pattern
- **AuthRepository**: Manages Firebase Authentication state and operations
- **FirestoreRepository**: Manages Firestore CRUD and real-time listeners
- **ImageStorageRepository**: Manages Firebase Storage uploads
- **Fragments**: Presentation layer (UI)
- **Flow/StateFlow**: Reactive data binding

### Real-Time Synchronization
- `FirestoreRepository.observeItemsForUser()` - Real-time item list for a user
- `FirestoreRepository.observeUser()` - Real-time user profile updates
- Changes on other devices appear automatically via Firestore listeners

### Two Advanced Features Implemented

1. **Firebase Cloud Messaging (FCM) for Push Notifications**
   - `AppFirebaseMessagingService` receives FCM messages
   - Automatically shows system notifications
   - Token saving hook for targeted device messaging
   - Next: Save FCM token to `users/{userId}.fcmToken` in onNewToken()

2. **Firebase Storage for Image Uploads**
   - `ImageStorageRepository.uploadProfileImage()` uploads to `images/profiles/{userId}.jpg`
   - Returns public download URL for use in Firestore
   - Next: Add UI file picker and save URL to user profile

## Common Issues & Troubleshooting

### Issue: "google-services.json not found"
**Solution:** Ensure the file is at `app/google-services.json` (exact path), not in `src/main/`

### Issue: "Unresolved reference: FirebaseAuth"
**Solution:** Run `./gradlew sync` or reload Gradle in Android Studio

### Issue: "Plugin with id 'com.google.gms.google-services' not found"
**Solution:** Ensure root `build.gradle.kts` has the plugin declared in the plugins block

### Issue: "JAVA_HOME is not set"
**Solution:** Install JDK and set JAVA_HOME environment variable, then restart terminal/IDE

### Issue: Google Sign-In fails with "Client ID not recognized"
**Solution:** Ensure web OAuth client ID is correct and matches your Firebase project

## Recommended Next Steps

1. ✅ Complete all 7 manual setup steps above
2. Run `./gradlew.bat clean assembleDebug` to verify build succeeds
3. Deploy to emulator/device and test sign-in flows
4. Implement email/password UI in Compose (ProfileFragment currently shows Google Sign-In only)
5. Replace deprecated `startActivityForResult` with Activity Result API:
   ```kotlin
   val googleSignInLauncher = registerForActivityResult(
       ActivityResultContracts.StartActivityForResult()
   ) { result ->
       // Handle result
   }
   ```
6. Add file picker for profile image upload using `ImageStorageRepository`
7. Hook up Firestore real-time listeners to UI to display synced data

## Support Resources

- [Firebase Android Documentation](https://firebase.google.com/docs/android/setup)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-overview.html)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firestore Real-Time Updates](https://firebase.google.com/docs/firestore/query-data/listen)
- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android)

## Summary Checklist

- [ ] Install JDK 11+ and set JAVA_HOME
- [ ] Download google-services.json to `app/`
- [ ] Create OAuth 2.0 Web Client ID in Google Cloud Console
- [ ] Update `ProfileFragment.kt` with web client ID
- [ ] Enable Email/Password auth in Firebase Console
- [ ] Enable Google auth in Firebase Console
- [ ] Create Firestore Database in test mode
- [ ] (Optional) Enable Cloud Storage
- [ ] (Optional) Enable Cloud Messaging
- [ ] Run `./gradlew.bat clean assembleDebug`
- [ ] Deploy to emulator/device and test sign-in

---

**Created:** May 10, 2026  
**Project:** Drivo - Firebase Integration Upgrade (Assignment #05)  
**Status:** Code complete, awaiting manual configuration steps

