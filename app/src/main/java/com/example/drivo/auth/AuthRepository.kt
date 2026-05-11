package com.example.drivo.auth

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Simple Auth repository using FirebaseAuth exposing auth state as Flow
 */
class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

	fun observeAuthState(): Flow<AuthState> = callbackFlow {
		val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
			val user = firebaseAuth.currentUser
			trySend(if (user != null) AuthState.SignedIn(user.uid, user.email) else AuthState.SignedOut)
		}
		auth.addAuthStateListener(listener)
		awaitClose { auth.removeAuthStateListener(listener) }
	}

	suspend fun signInWithEmail(email: String, password: String) = try {
		auth.signInWithEmailAndPassword(email, password).await()
		AuthResult.Success
	} catch (e: Exception) {
		AuthResult.Failure(e)
	}

	suspend fun createUserWithEmail(email: String, password: String) = try {
		auth.createUserWithEmailAndPassword(email, password).await()
		AuthResult.Success
	} catch (e: Exception) {
		AuthResult.Failure(e)
	}

	fun getGoogleSignInClient(activity: Activity, webClientId: String): GoogleSignInClient {
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(webClientId)
			.requestEmail()
			.build()
		return GoogleSignIn.getClient(activity, gso)
	}

	suspend fun handleGoogleSignInResult(data: Intent?): AuthResult {
		return try {
			val task: Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
			val account = task.await()
			val credential = GoogleAuthProvider.getCredential(account.idToken, null)
			auth.signInWithCredential(credential).await()
			AuthResult.Success
		} catch (e: Exception) {
			AuthResult.Failure(e)
		}
	}

	fun signOut() {
		auth.signOut()
	}
}

sealed class AuthResult {
	object Success : AuthResult()
	data class Failure(val error: Throwable) : AuthResult()
}

sealed class AuthState {
	object SignedOut : AuthState()
	data class SignedIn(val uid: String, val email: String?) : AuthState()
}


