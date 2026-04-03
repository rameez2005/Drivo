package com.example.drivo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.R
import com.example.drivo.adapters.DriverAdapter
import com.example.drivo.models.DataSource
import com.example.drivo.models.Driver

class DriverListFragment : Fragment() {
    private lateinit var adapter: DriverAdapter
    private lateinit var allDrivers: List<Driver>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_driver_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get all drivers
        allDrivers = DataSource.getDrivers()

        // Setup adapter
        adapter = DriverAdapter(requireContext(), allDrivers)

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_drivers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Setup SearchView
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDrivers(newText ?: "")
                return true
            }
        })
    }

    private fun filterDrivers(query: String) {
        val filtered = allDrivers.filter {
            it.fullName.contains(query, ignoreCase = true) ||
            it.phone.contains(query, ignoreCase = true) ||
            it.assignedVehicle.contains(query, ignoreCase = true)
        }
        adapter.updateList(filtered)
    }
}

