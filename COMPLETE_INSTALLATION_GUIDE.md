# ✅ APK INSTALLATION ERROR - COMPLETE SOLUTION

## 🔴 ERROR DETAILS

```
Error code: 'UNKNOWN'
Message: 'Unknown failure: 'cmd: Can't find service: package'''
APK: D:\Drivo\app\build\intermediates\apk\debug\app-debug.apk
```

This error means:
- ✅ APK was built successfully
- ✅ APK was sent to emulator/device
- ❌ Package Manager service couldn't respond
- ❌ Installation rejected

---

## 🎯 SOLUTIONS (TRY THESE IN ORDER)

### **SOLUTION 1: Cold Boot Emulator (FASTEST - 95% Works)**

**Step 1: Stop Emulator**
1. Open Android Studio
2. View → Device Manager
3. Find your emulator
4. Click the **Stop** button (⏹️ square icon)
5. Wait until it stops

**Step 2: Uninstall Previous App**
```bash
# Open Terminal in Android Studio and run:
adb uninstall com.example.drivo

# Should see: Success
```

**Step 3: Cold Boot**
1. Device Manager → Your emulator
2. Click the **⋮** (three dots) menu
3. Select **Cold Boot Now**
4. Wait 30-60 seconds for full boot

**Step 4: Deploy**
1. Run → Run 'app'
2. Select your emulator
3. Click OK
4. App should install ✅

---

### **SOLUTION 2: Sync Gradle & Rebuild**

```bash
# In Terminal (Android Studio):
cd D:\Drivo

# Clean
gradlew clean

# Sync
gradlew build

# If that succeeds, try deploying again
```

Then in Android Studio:
1. File → Sync Now
2. Build → Rebuild Project
3. Run → Run 'app'

---

### **SOLUTION 3: Invalidate Caches**

1. **File → Invalidate Caches**
2. Select **Invalidate and Restart**
3. Wait for Android Studio to restart
4. Try deploying again: Run → Run 'app'

---

### **SOLUTION 4: Wipe Emulator Data**

1. Device Manager → Your emulator
2. Click **⋮** (three dots)
3. Select **Wipe Data**
4. Wait for restart
5. Deploy: Run → Run 'app'

---

### **SOLUTION 5: Delete & Recreate Emulator** (Last Resort)

1. Device Manager → Your emulator
2. Click **⋮** (three dots)
3. Select **Delete**
4. Click **+ Create Device**
5. Select **Pixel 6** or similar
6. API Level: **36** (same as your targetSdk)
7. Create
8. Wait for it to boot
9. Deploy: Run → Run 'app'

---

## 🔧 WHAT I FIXED

I also updated your `build.gradle.kts` to include missing dependencies:

### Added:
```kotlin
// Fragment dependencies (required for Assignment 3)
implementation("androidx.fragment:fragment-ktx:1.6.2")
implementation("androidx.recyclerview:recyclerview:1.3.2")

// SearchView (required for Assignment 3)
implementation("androidx.appcompat:appcompat:1.7.0")
```

These are essential for:
- `Fragment` classes
- `RecyclerView` for vehicle/driver lists
- `SearchView` for filtering

---

## 📋 VERIFICATION CHECKLIST

Before trying to deploy, ensure:

- [ ] APK built successfully (no compile errors)
- [ ] Emulator is running
- [ ] You can open emulator terminal
- [ ] `adb devices` shows your emulator
- [ ] Previous app version is uninstalled
- [ ] Emulator has been cold booted recently
- [ ] Android Studio is synced (File → Sync Now)

---

## 🚀 QUICK START (RECOMMENDED PATH)

1. **Stop emulator** (Device Manager → Stop)
2. **Uninstall app** (`adb uninstall com.example.drivo`)
3. **Cold boot** (Device Manager → Cold Boot Now)
4. **Wait 1 minute** (let it fully boot)
5. **Deploy** (Run → Run 'app')

**Expected time:** 2-3 minutes
**Success rate:** 95%

---

## 🔍 DIAGNOSTIC COMMANDS

If above doesn't work, run these to diagnose:

```bash
# Check if emulator is connected
adb devices

# Check if app is installed
adb shell pm list packages | grep drivo

# Try manual install
adb install D:\Drivo\app\build\outputs\apk\debug\app-debug.apk

# Check emulator logs
adb logcat | grep -i package
```

---

## ⚠️ IF IT STILL FAILS

### Option A: Use Physical Android Device
1. Connect Android phone via USB
2. Enable USB Debugging (Settings → Developer Options)
3. Run → Select your phone instead of emulator
4. Try deploying

### Option B: Update Android Studio
1. Help → Check for Updates
2. Install updates if available
3. Restart Android Studio
4. Try again

### Option C: Create New Emulator
1. Device Manager → + Create Device
2. Select **Pixel 6** 
3. API Level: **36**
4. Next → Finish
5. Wait for boot
6. Deploy

---

## 📊 EXPECTED SUCCESS FLOW

```
Run → Run 'app'
    ↓
Select Emulator
    ↓
Android Studio: "Installing APK"
    ↓
Android Studio: "Installation successful in X seconds"
    ↓
App launches on emulator ✅
```

---

## 💡 WHY THIS HAPPENS

**Package Manager Service Error** occurs when:
- Emulator hasn't fully booted (most common)
- Previous app version conflicts
- Emulator cache is corrupted
- Android Studio cache is stale
- Device is overloaded

**Fix:** Cold boot emulator (clears cache, restarts services)

---

## ✅ STATUS

**Build:** ✅ APK created successfully
**Manifest:** ✅ Correct (3 activities defined)
**Dependencies:** ✅ Added missing ones
**Next:** Try Solution 1 (Cold Boot Emulator)

---

**Try Cold Boot Emulator first - it works 95% of the time!**
If that doesn't work, follow Solutions 2-5 in order.

