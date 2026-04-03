package com.example.drivo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.drivo.R
import com.example.drivo.activities.MainActivity

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get owner name from arguments Bundle
        val ownerName = arguments?.getString("OWNER_NAME") ?: "Owner"
        val companyName = arguments?.getString("COMPANY_NAME") ?: "TransFleet"

        // Set toolbar title and subtitle
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = companyName
        toolbar.subtitle = "Welcome, $ownerName"

        // Setup click listeners for dashboard cards
        setupCardClickListeners(view)
    }

    private fun setupCardClickListeners(view: View) {
        // Emergency Dispatch card
        view.findViewById<LinearLayout>(R.id.card_emergency_dispatch).setOnClickListener {
            // Navigate to a hypothetical emergency screen or show a message
            // For now, we don't have a specific screen, so we just show the vehicles (similar to fleet)
            (activity as MainActivity).loadFragment(com.example.drivo.fragments.VehicleListFragment())
        }

        // Fleet card - navigate to vehicle list
        view.findViewById<LinearLayout>(R.id.card_fleet).setOnClickListener {
            (activity as MainActivity).loadFragment(com.example.drivo.fragments.VehicleListFragment())
        }

        // Drivers card - navigate to driver list
        view.findViewById<LinearLayout>(R.id.card_drivers).setOnClickListener {
            (activity as MainActivity).loadFragment(com.example.drivo.fragments.DriverListFragment())
        }

        // Salary card - for now just show a message or navigate to driver list
        view.findViewById<LinearLayout>(R.id.card_salary).setOnClickListener {
            // Navigate to driver list (where salary info could be shown)
            (activity as MainActivity).loadFragment(com.example.drivo.fragments.DriverListFragment())
        }

        // Reports card - for now navigate to vehicle list
        view.findViewById<LinearLayout>(R.id.card_reports).setOnClickListener {
            (activity as MainActivity).loadFragment(com.example.drivo.fragments.VehicleListFragment())
        }

        // Settings card - navigate to settings screen if it exists, otherwise do nothing
        view.findViewById<LinearLayout>(R.id.card_settings).setOnClickListener {
            // Settings screen not implemented in Assignment 3
            // Can be added in future
        }
    }
}

