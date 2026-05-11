package com.example.drivo.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

/**
 * Helper for uploading images to Firebase Storage. Returns download URL after upload.
 */
class ImageStorageRepository(private val storage: FirebaseStorage = FirebaseStorage.getInstance()) {
	private val imagesRef = storage.reference.child("images")

	suspend fun uploadProfileImage(userId: String, fileUri: Uri): String {
		val ref = imagesRef.child("profiles/$userId.jpg")
		ref.putFile(fileUri).await()
		return ref.downloadUrl.await().toString()
	}
}


