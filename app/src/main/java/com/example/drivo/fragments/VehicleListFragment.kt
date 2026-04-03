package com.example.drivo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.R
import com.example.drivo.adapters.VehicleAdapter
import com.example.drivo.models.DataSource
import com.example.drivo.models.Vehicle

class VehicleListFragment : Fragment() {
    private lateinit var adapter: VehicleAdapter
    private lateinit var allVehicles: List<Vehicle>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get all vehicles
        allVehicles = DataSource.getVehicles()

        // Setup adapter
        adapter = VehicleAdapter(requireContext(), allVehicles)

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_vehicles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Setup SearchView
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterVehicles(newText ?: "")
                return true
            }
        })

        // Setup Filter Chips (RadioGroup)
        setupFilterChips(view)
    }

    private fun filterVehicles(query: String) {
        val filtered = allVehicles.filter {
            it.registrationNumber.contains(query, ignoreCase = true) ||
            it.make.contains(query, ignoreCase = true) ||
            it.model.contains(query, ignoreCase = true) ||
            it.assignedDriver.contains(query, ignoreCase = true)
        }
        adapter.updateList(filtered)
    }

    private fun setupFilterChips(view: View) {
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_filter)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val filtered = when (checkedId) {
                R.id.rb_active -> allVehicles.filter { it.status == "ACTIVE" }
                R.id.rb_maintenance -> allVehicles.filter { it.status == "MAINTENANCE" }
                R.id.rb_retired -> allVehicles.filter { it.status == "RETIRED" }
                else -> allVehicles  // All selected
            }
            adapter.updateList(filtered)
        }
    }
}

