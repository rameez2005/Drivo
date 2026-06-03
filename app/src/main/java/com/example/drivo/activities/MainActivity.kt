package com.example.drivo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.drivo.R
import com.example.drivo.data.FirestoreRepository
import com.example.drivo.fragments.DashboardFragment
import com.example.drivo.fragments.ApiFeedFragment
import com.example.drivo.fragments.VehicleListFragment
import com.example.drivo.fragments.DriverListFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var ownerName: String = "Owner"
    private var companyName: String = "TransFleet"
    private val firestoreRepository by lazy { FirestoreRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get Intent extras from SplashActivity
        ownerName = intent.getStringExtra("OWNER_NAME") ?: "Owner"
        companyName = intent.getStringExtra("COMPANY_NAME") ?: "TransFleet"

        // Pass to DashboardFragment via Bundle
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, createDashboardFragment())
                .commit()
        }

        // Set up bottom navigation
        setupBottomNavigation()

        // Setup sign out button
        val btnSignOut = findViewById<android.widget.Button>(R.id.btnSignOut)
        btnSignOut.setOnClickListener {
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
            // Go back to SignInActivity
            val intent = android.content.Intent(this, com.example.drivo.activities.SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        updatePresence(isOnline = true)
        flushPendingFcmTokenIfAny()
    }

    override fun onStop() {
        updatePresence(isOnline = false)
        super.onStop()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> loadFragment(createDashboardFragment(), addToBackStack = false)
                R.id.nav_vehicles -> loadFragment(VehicleListFragment(), addToBackStack = false)
                R.id.nav_drivers -> loadFragment(DriverListFragment(), addToBackStack = false)
                R.id.nav_api -> loadFragment(ApiFeedFragment(), addToBackStack = false)
            }
            true
        }
    }

    private fun createDashboardFragment(): DashboardFragment {
        return DashboardFragment().apply {
            arguments = Bundle().apply {
                putString("OWNER_NAME", ownerName)
                putString("COMPANY_NAME", companyName)
            }
        }
    }

    fun loadFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun updatePresence(isOnline: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                firestoreRepository.updateUserPresence(userId, isOnline)
            } catch (_: Exception) {
                // Best-effort presence update; app should keep working if Firestore is temporarily unavailable.
            }
        }
    }

    private fun flushPendingFcmTokenIfAny() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val token = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_PENDING_TOKEN, null) ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                firestoreRepository.updateUserToken(userId, token)
                getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit()
                    .remove(KEY_PENDING_TOKEN)
                    .apply()
            } catch (_: Exception) {
                // Keep retrying on later launches.
            }
        }
    }

    companion object {
        private const val PREFS = "fcm_prefs"
        private const val KEY_PENDING_TOKEN = "pending_fcm_token"
    }
}

