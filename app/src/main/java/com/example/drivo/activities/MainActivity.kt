package com.example.drivo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.drivo.R
import com.example.drivo.fragments.DashboardFragment
import com.example.drivo.fragments.VehicleListFragment
import com.example.drivo.fragments.DriverListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get Intent extras from SplashActivity
        val ownerName = intent.getStringExtra("OWNER_NAME") ?: "Owner"
        val companyName = intent.getStringExtra("COMPANY_NAME") ?: "TransFleet"

        // Pass to DashboardFragment via Bundle
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putString("OWNER_NAME", ownerName)
            bundle.putString("COMPANY_NAME", companyName)

            val dashboardFragment = DashboardFragment()
            dashboardFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, dashboardFragment)
                .commit()
        }

        // Set up bottom navigation
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> loadFragment(DashboardFragment())
                R.id.nav_vehicles -> loadFragment(VehicleListFragment())
                R.id.nav_drivers -> loadFragment(DriverListFragment())
            }
            true
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

