package com.example.drivo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.drivo.R
import com.example.drivo.fragments.DashboardFragment
import com.example.drivo.fragments.ApiFeedFragment
import com.example.drivo.fragments.VehicleListFragment
import com.example.drivo.fragments.DriverListFragment

class MainActivity : AppCompatActivity() {
    private var ownerName: String = "Owner"
    private var companyName: String = "TransFleet"

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
}

