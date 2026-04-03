package com.example.drivo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.drivo.R
import com.example.drivo.models.Driver

class DriverDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_driver_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Driver from Bundle
        val driver = arguments?.getSerializable("DRIVER_DATA") as? Driver

        driver?.let {
            // Populate all views with driver data
            view.findViewById<TextView>(R.id.tv_driver_name).text = it.fullName
            view.findViewById<TextView>(R.id.tv_phone).text = it.phone
            view.findViewById<TextView>(R.id.tv_license_number).text = it.licenseNumber
            view.findViewById<TextView>(R.id.tv_license_expiry).text = it.licenseExpiry
            view.findViewById<TextView>(R.id.tv_vehicle).text = it.assignedVehicle
            view.findViewById<TextView>(R.id.tv_route).text = it.assignedRoute
            view.findViewById<TextView>(R.id.tv_availability).text = it.availabilityStatus
            view.findViewById<TextView>(R.id.tv_attendance).text = "${it.attendanceDays} / ${it.totalWorkingDays} days"
            view.findViewById<TextView>(R.id.tv_pending_dues).text = it.pendingDues
            view.findViewById<TextView>(R.id.tv_performance_rating).text = it.performanceRating
        }
    }
}

