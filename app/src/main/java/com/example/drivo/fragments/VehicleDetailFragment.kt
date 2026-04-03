package com.example.drivo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.drivo.R
import com.example.drivo.models.Vehicle

class VehicleDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Vehicle from Bundle
        val vehicle = arguments?.getSerializable("VEHICLE_DATA") as? Vehicle

        vehicle?.let {
            // Populate all views with vehicle data
            view.findViewById<TextView>(R.id.tv_reg_number).text = it.registrationNumber
            view.findViewById<TextView>(R.id.tv_make_model).text = "${it.make} ${it.model} ${it.year}"
            view.findViewById<TextView>(R.id.tv_status).text = it.status
            view.findViewById<TextView>(R.id.tv_driver).text = it.assignedDriver
            view.findViewById<TextView>(R.id.tv_route).text = it.assignedRoute
            view.findViewById<TextView>(R.id.tv_last_maintenance).text = it.lastMaintenance
            view.findViewById<TextView>(R.id.tv_maintenance_cost).text = it.maintenanceCost
        }
    }
}

