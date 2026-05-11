package com.example.drivo.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Firestore repository providing real-time listeners and basic CRUD for a simple `items` collection
 * and a `users` collection. Uses coroutine-friendly APIs and Flow with callbackFlow for real-time updates.
 */
class FirestoreRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    private val users = firestore.collection("users")
    private val items = firestore.collection("items")

    fun observeItemsForUser(userId: String): Flow<List<DocumentSnapshot>> = callbackFlow {
        val listener = items.whereEqualTo("ownerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val docs = snapshot?.documents ?: emptyList()
                trySend(docs)
            }
        awaitClose { listener.remove() }
    }

    fun observeUser(userId: String): Flow<DocumentSnapshot?> = callbackFlow {
        val listener = users.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot)
        }
        awaitClose { listener.remove() }
    }

    suspend fun createOrUpdateUser(userId: String, data: Map<String, Any>) {
        users.document(userId).set(data, SetOptions.merge()).await()
    }

    suspend fun updateUserPresence(userId: String, isOnline: Boolean, token: String? = null) {
        val data = mutableMapOf<String, Any>(
            "isOnline" to isOnline,
            "lastSeen" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )
        token?.let { data["fcmToken"] = it }
        users.document(userId).set(data, SetOptions.merge()).await()
    }

    suspend fun updateUserToken(userId: String, token: String) {
        users.document(userId).set(
            mapOf(
                "fcmToken" to token,
                "lastSeen" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            ),
            SetOptions.merge()
        ).await()
    }

    suspend fun addItem(data: Map<String, Any>) {
        items.add(data).await()
    }

    suspend fun deleteItem(itemId: String) {
        items.document(itemId).delete().await()
    }
}

