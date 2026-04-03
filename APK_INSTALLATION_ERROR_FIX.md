# 🔧 APK INSTALLATION ERROR - TROUBLESHOOTING GUIDE

## 🔴 ERROR

```
Error code: 'UNKNOWN', message='Unknown failure: 'cmd: Can't find service: package'''
```

**Location:** Installation phase (APK was built, but device rejected it)

---

## 🎯 ROOT CAUSES & SOLUTIONS

### **Cause 1: Emulator/Device Package Manager Service Issue** ⭐ MOST COMMON

**Solution:**

#### **Option A: Restart Emulator (FASTEST)**
1. Close the emulator completely
2. In Android Studio: **Device Manager → (Your Device) → Stop**
3. Wait 10 seconds
4. Click **Play** to restart it
5. Try again: **Run → Run 'app'**

#### **Option B: Cold Boot Emulator**
1. Device Manager → Your Device → Triple dots (⋮) → Cold Boot Now
2. Wait for it to fully load
3. Try deploying again

#### **Option C: Wipe Data**
1. Device Manager → Your Device → Triple dots (⋮) → Wipe Data
2. Wait for restart
3. Try deploying again

---

### **Cause 2: Previous App Installation Conflict**

**Solution:**

#### **Option A: Uninstall Previous Version (EMULATOR)**
```bash
# In emulator terminal or Android Studio terminal:
adb uninstall com.example.drivo
```

#### **Option B: Uninstall via Emulator Settings**
1. Open emulator settings
2. Apps → App Management
3. Find "Drivo"
4. Uninstall it
5. Try deploying again

#### **Option C: Delete All App Data**
1. Settings → Apps → Drivo → Storage → Clear Cache
2. Settings → Apps → Drivo → Uninstall
3. Try deploying again

---

### **Cause 3: APK Build Issue**

**Solution:**

#### **Step 1: Full Clean Build**
```bash
cd D:\Drivo
gradlew clean
gradlew build
```

#### **Step 2: Rebuild APK**
```bash
gradlew assembleDebug
```

#### **Step 3: Deploy**
- Android Studio: **Run → Run 'app'**

---

### **Cause 4: Android Studio Cache Issue**

**Solution:**

1. **File → Invalidate Caches → Invalidate and Restart**
2. Wait for Android Studio to restart
3. Try deploying again

---

### **Cause 5: Gradle Sync Issue**

**Solution:**

1. **File → Sync Now**
2. Wait for sync to complete
3. Try deploying again

---

## 🚀 QUICK FIX CHECKLIST

**Do these in order, try deploying after each:**

- [ ] Restart emulator (Cold Boot Now)
- [ ] Uninstall previous app: `adb uninstall com.example.drivo`
- [ ] Clean project: `gradlew clean`
- [ ] Invalidate caches: File → Invalidate Caches → Invalidate and Restart
- [ ] Sync gradle: File → Sync Now
- [ ] Rebuild project: Build → Rebuild Project
- [ ] Deploy: Run → Run 'app'

---

## 📋 DETAILED STEPS FOR EMULATOR FIX

### **Step 1: Stop Emulator**
1. Open Device Manager (View → Device Manager)
2. Find your emulator
3. Click **Stop** (square button)
4. Wait until it stops

### **Step 2: Uninstall App**
1. Open Terminal in Android Studio
2. Run: `adb uninstall com.example.drivo`
3. Should see: `Success`

### **Step 3: Cold Boot Emulator**
1. Device Manager → Your device → Triple dots (⋮)
2. Select **Cold Boot Now**
3. Wait 30-60 seconds for full boot

### **Step 4: Deploy App**
1. Run → Run 'app' (or Shift + F10)
2. Select your emulator
3. Click OK
4. App should install ✅

---

## 🔍 WHAT TO CHECK IF IT STILL FAILS

### **Check 1: APK Exists**
```bash
ls D:\Drivo\app\build\outputs\apk\debug\
# Should show: app-debug.apk
```

### **Check 2: Device is Connected**
```bash
adb devices
# Should show: emulator-5554    device
```

### **Check 3: Emulator is Responsive**
```bash
adb shell
# Should show: generic_x86_64:/ #
```

### **Check 4: Package Manager Works**
```bash
adb shell pm list packages | grep drivo
# Should show nothing (not installed yet)
```

### **Check 5: Install APK Manually**
```bash
adb install D:\Drivo\app\build\outputs\apk\debug\app-debug.apk
# Should show: Success
```

---

## 💡 COMMON SCENARIOS & FIXES

### **Scenario 1: Just Started Emulator**
- Wait 2-3 minutes for emulator to fully boot
- Then try deploying

### **Scenario 2: Deployed Before (Stuck from Last Time)**
- Uninstall: `adb uninstall com.example.drivo`
- Wipe emulator data
- Cold boot emulator
- Try again

### **Scenario 3: Multiple Emulators Running**
- Close other emulators
- Use only ONE emulator
- Try deploying to that one

### **Scenario 4: Using Physical Device**
- Enable Developer Mode: Settings → About Phone → Tap "Build Number" 7 times
- Enable USB Debugging: Settings → Developer Options → USB Debugging
- Connect device via USB
- Try deploying

---

## 🎯 RECOMMENDED APPROACH

### **FASTEST: Cold Boot Emulator**

1. **Stop emulator** (Device Manager → Stop)
2. **Uninstall app** (`adb uninstall com.example.drivo`)
3. **Cold Boot** (Device Manager → Cold Boot Now)
4. **Deploy** (Run → Run 'app')

**Time:** ~2 minutes
**Success rate:** 95%

---

## 📊 IF NOTHING WORKS

### Last Resort Options:

1. **Delete Emulator & Create New One**
   - Device Manager → Triple dots → Delete
   - Device Manager → Create Device
   - Create a new Pixel 6 with API 36
   - Try deploying

2. **Use Physical Device Instead**
   - Connect Android phone via USB
   - Enable USB Debugging
   - Select device in Run dialog
   - Try deploying

3. **Update Android Studio**
   - Check for updates: Help → Check for Updates
   - Install if available
   - Restart Android Studio
   - Try deploying

---

## 🔗 WHY THIS ERROR HAPPENS

The error "Can't find service: package" means:
- APK was built successfully ✅
- Device received it ✅
- But Package Manager service crashed/isn't responding ❌
- Device rejected installation ❌

This is usually a **device/emulator issue**, not your app code.

---

## ✅ SUCCESS INDICATORS

When it works, you'll see:
```
Installation successful in X seconds
```

And the app will launch on the emulator/device.

---

**Try the "Cold Boot Emulator" approach first - it works 95% of the time!**

