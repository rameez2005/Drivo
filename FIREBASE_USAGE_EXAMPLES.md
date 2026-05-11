# Firebase Integration - Usage Examples

## Complete Example: Sign-In & Real-Time Sync

This example shows how to wire all the components together in a practical Fragment.

### Full Example Fragment

```kotlin
package com.example.drivo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.R
import com.example.drivo.auth.AuthRepository
import com.example.drivo.auth.AuthState
import com.example.drivo.data.FirestoreRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Complete example showing Firebase Auth + Firestore real-time sync
 */
class ExampleAuthSyncFragment : Fragment() {

    private val authRepo = AuthRepository()
    private val firestoreRepo = FirestoreRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth_sync, container, false) // hypothetical layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userEmailView = view.findViewById<TextView>(R.id.user_email)
        val signOutBtn = view.findViewById<Button>(R.id.sign_out_btn)
        val itemsList = view.findViewById<RecyclerView>(R.id.items_list)

        // Observe auth state and update UI
        lifecycleScope.launch {
            authRepo.observeAuthState().collectLatest { authState ->
                when (authState) {
                    is AuthState.SignedOut -> {
                        userEmailView.text = "Not signed in"
                        signOutBtn.isEnabled = false
                    }
                    is AuthState.SignedIn -> {
                        userEmailView.text = "Welcome: ${authState.email}"
                        signOutBtn.isEnabled = true

                        // Save user profile to Firestore
                        firestoreRepo.createOrUpdateUser(
                            authState.uid,
                            mapOf(
                                "email" to (authState.email ?: ""),
                                "displayName" to "User_${authState.uid.take(4)}"
                            )
                        )

                        // Observe items for this user in real-time
                        observeUserItems(authState.uid, itemsList)
                    }
                }
            }
        }

        // Sign out
        signOutBtn.setOnClickListener {
            authRepo.signOut()
        }
    }

    private fun observeUserItems(userId: String, recyclerView: RecyclerView) {
        lifecycleScope.launch {
            firestoreRepo.observeItemsForUser(userId).collectLatest { snapshots ->
                // Update UI with real-time items
                val items = snapshots.map { snapshot ->
                    ItemDisplay(
                        id = snapshot.id,
                        title = snapshot.getString("title") ?: "Untitled",
                        desc = snapshot.getString("description") ?: ""
                    )
                }

                val adapter = ItemAdapter(items)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    data class ItemDisplay(val id: String, val title: String, val desc: String)

    class ItemAdapter(private val items: List<ItemDisplay>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
        }

        override fun getItemCount() = items.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: ItemDisplay) {
                val text1 = itemView.findViewById<TextView>(android.R.id.text1)
                val text2 = itemView.findViewById<TextView>(android.R.id.text2)
                text1.text = item.title
                text2.text = item.desc
            }
        }
    }
}
```

---

## Email/Password Sign-In Example

```kotlin
// In your Fragment/Activity:
private val authRepo = AuthRepository()

fun handleEmailSignUp(email: String, password: String) {
    lifecycleScope.launch {
        val result = authRepo.createUserWithEmail(email, password)
        when (result) {
            is AuthResult.Success -> {
                showMessage("Account created!")
                // User is auto-signed-in after creation
            }
            is AuthResult.Failure -> {
                showMessage("Error: ${result.error.message}")
            }
        }
    }
}

fun handleEmailSignIn(email: String, password: String) {
    lifecycleScope.launch {
        val result = authRepo.signInWithEmail(email, password)
        when (result) {
            is AuthResult.Success -> {
                showMessage("Signed in!")
            }
            is AuthResult.Failure -> {
                showMessage("Error: ${result.error.message}")
            }
        }
    }
}
```

---

## Real-Time Firestore Sync Example

```kotlin
// Observe items for logged-in user
lifecycleScope.launch {
    authRepo.observeAuthState().collectLatest { authState ->
        if (authState is AuthState.SignedIn) {
            // Auto-updates whenever ANY change occurs in Firestore
            firestoreRepo.observeItemsForUser(authState.uid).collectLatest { docs ->
                docs.forEach { doc ->
                    val title = doc.getString("title")
                    val ownerId = doc.getString("ownerId")
                    Log.d("Firestore", "Item $title owned by $ownerId")
                }
            }
        }
    }
}
```

---

## Firebase Storage Image Upload Example

```kotlin
private val imageStorageRepo = ImageStorageRepository()

// After user picks a file via file picker
fun uploadProfileImage(imageUri: Uri, userId: String) {
    lifecycleScope.launch {
        try {
            val downloadUrl = imageStorageRepo.uploadProfileImage(userId, imageUri)
            
            // Save URL to Firestore user profile
            firestoreRepo.createOrUpdateUser(userId, mapOf(
                "avatarUrl" to downloadUrl
            ))
            
            showMessage("Profile picture updated!")
        } catch (e: Exception) {
            showMessage("Upload failed: ${e.message}")
        }
    }
}
```

---

## Receiving FCM Push Notifications Example

The service is already implemented, but here's how to trigger it:

### From Firebase Console (Test)
1. Go to Firebase Console → Cloud Messaging
2. Click "Send your first message"
3. Enter title/body, test message
4. Select your device
5. Device receives notification in notification tray

### From Server (Production)
```
POST https://fcm.googleapis.com/fcm/send

{
  "to": "DEVICE_FCM_TOKEN",
  "notification": {
    "title": "Drivo Update",
    "body": "Your vehicle needs maintenance"
  }
}
```

---

## Creating Firestore Collections Programmatically

```kotlin
// Create user profile
lifecycleScope.launch {
    authRepo.observeAuthState().collectLatest { authState ->
        if (authState is AuthState.SignedIn) {
            firestoreRepo.createOrUpdateUser(authState.uid, mapOf(
                "displayName" to "John Doe",
                "email" to "john@example.com",
                "avatarUrl" to "",
                "fcmToken" to "", // fill from FCM callback
                "createdAt" to System.currentTimeMillis()
            ))
        }
    }
}

// Create an item
lifecycleScope.launch {
    firestoreRepo.addItem(mapOf(
        "ownerId" to userId,
        "title" to "Vehicle Maintenance",
        "description" to "Oil change needed",
        "updatedAt" to System.currentTimeMillis()
    ))
}

// Delete an item
lifecycleScope.launch {
    firestoreRepo.deleteItem(itemId)
}
```

---

## Manual Testing Checklist

### Test Scenario 1: Basic Auth Flow
- [ ] App starts, shows "Not signed in" on ProfileFragment
- [ ] Tap Google Sign-In button
- [ ] Complete Google flow in browser
- [ ] Return to app, shows "Signed in as: user@gmail.com"
- [ ] User appears in Firebase Console → Authentication
- [ ] Tap Sign out button
- [ ] Returns to "Not signed in"

### Test Scenario 2: Real-Time Sync
- [ ] User A signs in, opens app (watches items)
- [ ] User B adds item in Firebase Console: `items/{id} { ownerId: userId_A, title: "Test" }`
- [ ] User A's item list updates automatically (no refresh needed)
- [ ] User B deletes the item in Console
- [ ] User A's list updates automatically (item removed)

### Test Scenario 3: Profile Persistence
- [ ] Sign in once
- [ ] Close app
- [ ] Reopen app
- [ ] Still signed in (auth state persisted)

### Test Scenario 4: Image Upload
- [ ] Implement file picker on Profile Screen
- [ ] Select image
- [ ] Call `imageStorageRepo.uploadProfileImage(userId, imageUri)`
- [ ] URL saved to Firestore
- [ ] Can view in Firebase Console → Storage

### Test Scenario 5: Push Notifications
- [ ] Build and run app
- [ ] Firebase Console → Cloud Messaging → Send test message
- [ ] Select device
- [ ] Device receives notification in tray
- [ ] Click notification → opens MainActivity

---

## Architecture Diagrams

### Authentication Flow
```
Firebase Auth
    ↓
AuthRepository (coroutines suspend functions)
    ↓
ProfileFragment (Compose UI)
    ↓
Auth State Flow (observeAuthState)
    ↓
Real-time UI updates
```

### Firestore Real-Time Sync Flow
```
User Action (add/delete item)
    ↓
Firestore updates
    ↓
Listener detects change
    ↓
Flow emits new data
    ↓
Fragment receives in collectLatest
    ↓
UI updates
```

### Storage Upload & Retrieval
```
User picks image
    ↓
imageStorageRepo.uploadProfileImage()
    ↓
Firebase Storage (images/profiles/{userId}.jpg)
    ↓
Get download URL
    ↓
Save URL to Firestore users/{userId}.avatarUrl
```

---

## Common Patterns

### Pattern 1: Sign In → Save Profile → Listen to Data
```kotlin
lifecycleScope.launch {
    // 1. Sign in
    authRepo.observeAuthState().collectLatest { state ->
        if (state is AuthState.SignedIn) {
            // 2. Save profile
            firestoreRepo.createOrUpdateUser(state.uid, profileData)
            
            // 3. Listen to items
            firestoreRepo.observeItemsForUser(state.uid).collectLatest { items ->
                updateUI(items)
            }
        }
    }
}
```

### Pattern 2: Error Handling
```kotlin
lifecycleScope.launch {
    try {
        val result = authRepo.signInWithEmail(email, password)
        when (result) {
            is AuthResult.Success -> handleSuccess()
            is AuthResult.Failure -> handleError(result.error)
        }
    } catch (e: Exception) {
        showError("Unexpected error: ${e.message}")
    }
}
```

### Pattern 3: Combining Multiple Flows
```kotlin
lifecycleScope.launch {
    authRepo.observeAuthState().collectLatest { authState ->
        if (authState is AuthState.SignedIn) {
            combine(
                firestoreRepo.observeUser(authState.uid),
                firestoreRepo.observeItemsForUser(authState.uid)
            ) { user, items ->
                Pair(user, items)
            }.collectLatest { (user, items) ->
                updateUIWithBoth(user, items)
            }
        }
    }
}
```

---

## Recommended Layout Files (XML)

### fragment_auth_sync.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/user_email"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Not signed in"
        android:textSize="18sp" />

    <Button
        android:id="@+id/sign_out_btn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Sign Out" />

    <RecyclerView
        android:id="@+id/items_list"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />
</LinearLayout>
```

---

**That's it!** These examples show complete working implementations for all Firebase features.

