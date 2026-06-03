package com.example.drivo.activities

import android.Manifest
import android.content.pm.PackageManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.drivo.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        requestNotificationPermissionIfNeeded()

        Handler(Looper.getMainLooper()).postDelayed({
            // If user is signed in, go to MainActivity; otherwise send to SignInActivity
            val firebaseAuthClass = try {
                com.google.firebase.auth.FirebaseAuth::class
            } catch (e: Exception) {
                null
            }

            val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

            val intent = if (currentUser != null) {
                Intent(this, MainActivity::class.java).apply {
                    putExtra("OWNER_NAME", "Ahmed Khan")       // hardcoded owner name
                    putExtra("COMPANY_NAME", "TransFleet Co.")
                }
            } else {
                Intent(this, SignInActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQ_POST_NOTIFICATIONS
                )
            }
        }
    }

    companion object {
        private const val REQ_POST_NOTIFICATIONS = 1001
    }
}

