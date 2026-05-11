package com.example.drivo.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.drivo.auth.AuthRepository
import com.example.drivo.auth.AuthState
import com.example.drivo.data.FirestoreRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * A Fragment that demonstrates integrating Jetpack Compose inside existing Fragment-based app.
 * Shows a simple profile/settings compose screen and hooks into AuthRepository + FirestoreRepository.
 */
class ProfileFragment : Fragment() {

	private val authRepo = AuthRepository()
	private val firestoreRepo = FirestoreRepository()

	// Provide your webClientId (from Firebase console -> OAuth 2.0 client ID) when using Google Sign-In
	private val webClientId: String = "238617994612-9bjce5uqpd4tnbbrlpnftajqf1uo3185.apps.googleusercontent.com"

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				MaterialTheme {
					ProfileScreen(authRepo = authRepo, onSignOut = {
						authRepo.signOut()
					}, onStartGoogleSignIn = {
						// Launch Google Sign-In flow
						val client: GoogleSignInClient = authRepo.getGoogleSignInClient(requireActivity() as Activity, webClientId)
						val intent = client.signInIntent
						startActivityForResult(intent, REQ_GOOGLE_SIGN_IN)
					})
				}
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == REQ_GOOGLE_SIGN_IN && resultCode == Activity.RESULT_OK) {
			lifecycleScope.launch {
				val result = authRepo.handleGoogleSignInResult(data)
				// simple handling; in production show errors to user
			}
		}
	}

	companion object {
		private const val REQ_GOOGLE_SIGN_IN = 9001
	}
}

@Composable
private fun ProfileScreen(authRepo: AuthRepository, onSignOut: () -> Unit, onStartGoogleSignIn: () -> Unit) {
	val authStateFlow = authRepo.observeAuthState()
	val authState by authStateFlow.catch { /* ignore errors for UI */ }.collectAsState(initial = AuthState.SignedOut)

	Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
		when (authState) {
			is AuthState.SignedOut -> {
				Text(text = "Not signed in", style = MaterialTheme.typography.titleMedium)
				Button(onClick = onStartGoogleSignIn, modifier = Modifier.padding(top = 8.dp)) {
					Text("Sign in with Google")
				}
			}
			is AuthState.SignedIn -> {
				val user = authState as AuthState.SignedIn
				Text(text = "Signed in as: ${user.email}", style = MaterialTheme.typography.titleMedium)
				Button(onClick = onSignOut, modifier = Modifier.padding(top = 8.dp)) {
					Text("Sign out")
				}
			}
		}
	}
}


