package com.example.drivo.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.activities.MainActivity
import com.example.drivo.R
import com.example.drivo.adapters.VehicleAdapter
import com.example.drivo.data.local.DrivoDbHelper
import com.example.drivo.data.repository.VehicleRepository
import com.example.drivo.models.Vehicle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class VehicleListFragment : Fragment() {

    private companion object {
        private const val TAG = "VehicleListFragment"
    }

    private lateinit var repository: VehicleRepository
    private lateinit var adapter: VehicleAdapter
    private var searchQuery: String = ""
    private var statusFilter: String = DrivoDbHelper.FILTER_ALL
    private var sortOrder: String = DrivoDbHelper.SORT_RECENT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = VehicleRepository(requireContext())

        adapter = VehicleAdapter(requireContext(), emptyList()) { vehicle ->
            val bundle = Bundle().apply { putSerializable("VEHICLE_DATA", vehicle) }
            val detailFragment = VehicleDetailFragment().apply { arguments = bundle }
            (activity as? MainActivity)?.loadFragment(detailFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_vehicles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText.orEmpty().trim()
                loadVehicles()
                return true
            }
        })

        setupFilterChips(view)
        setupSortSpinner(view)
        setupAddVehicle(view)

        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                repository.seedIfNeeded()
                loadVehicles()
            }.onFailure { error ->
                Log.e(TAG, "Failed to initialize fleet list", error)
                if (isAdded) {
                    Toast.makeText(requireContext(), R.string.vehicle_load_failed, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadVehicles()
    }

    private fun setupFilterChips(view: View) {
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_filter)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            statusFilter = when (checkedId) {
                R.id.rb_active -> "ACTIVE"
                R.id.rb_maintenance -> "MAINTENANCE"
                R.id.rb_retired -> "RETIRED"
                else -> DrivoDbHelper.FILTER_ALL
            }
            loadVehicles()
        }
    }

    private fun setupSortSpinner(view: View) {
        val sortSpinner = view.findViewById<Spinner>(R.id.spinner_sort)
        sortSpinner.setSelection(0)
        sortSpinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, itemView: View?, position: Int, id: Long) {
                sortOrder = when (position) {
                    1 -> DrivoDbHelper.SORT_YEAR_DESC
                    2 -> DrivoDbHelper.SORT_MAINTENANCE_DESC
                    3 -> DrivoDbHelper.SORT_REG_ASC
                    else -> DrivoDbHelper.SORT_RECENT
                }
                loadVehicles()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
        })
    }

    private fun setupAddVehicle(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fab_add_vehicle).setOnClickListener {
            showVehicleFormDialog()
        }
    }

    private fun showVehicleFormDialog() {
        val formView = layoutInflater.inflate(R.layout.dialog_vehicle_form, null)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_vehicle))
            .setView(formView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val newVehicle = readVehicleFromForm(formView) ?: return@setPositiveButton

                viewLifecycleOwner.lifecycleScope.launch {
                    val result = repository.addVehicle(newVehicle)
                    if (result > 0) {
                        Toast.makeText(requireContext(), R.string.vehicle_saved, Toast.LENGTH_SHORT).show()
                        loadVehicles()
                    } else {
                        Toast.makeText(requireContext(), R.string.vehicle_save_failed, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun readVehicleFromForm(formView: View): Vehicle? {
        val registration = formView.findViewById<EditText>(R.id.et_registration).text.toString().trim()
        val make = formView.findViewById<EditText>(R.id.et_make).text.toString().trim()
        val model = formView.findViewById<EditText>(R.id.et_model).text.toString().trim()
        val year = formView.findViewById<EditText>(R.id.et_year).text.toString().trim().toIntOrNull() ?: 0
        val type = formView.findViewById<EditText>(R.id.et_type).text.toString().trim()
        val status = formView.findViewById<EditText>(R.id.et_status).text.toString().trim().uppercase()
        val driver = formView.findViewById<EditText>(R.id.et_driver).text.toString().trim()
        val route = formView.findViewById<EditText>(R.id.et_route).text.toString().trim()
        val lastMaintenance = formView.findViewById<EditText>(R.id.et_last_maintenance).text.toString().trim()
        val maintenanceCost = formView.findViewById<EditText>(R.id.et_maintenance_cost).text.toString().trim()

        if (registration.isBlank() || make.isBlank() || model.isBlank() || year <= 0) {
            Toast.makeText(requireContext(), R.string.validation_required_fields, Toast.LENGTH_SHORT).show()
            return null
        }

        return Vehicle(
            vehicleId = "0",
            registrationNumber = registration,
            make = make,
            model = model,
            year = year,
            vehicleType = type.ifBlank { "BUS" },
            status = status.ifBlank { "ACTIVE" },
            assignedDriver = driver.ifBlank { "Unassigned" },
            assignedRoute = route.ifBlank { "No Route" },
            lastMaintenance = lastMaintenance.ifBlank { "N/A" },
            maintenanceCost = "PKR ${maintenanceCost.ifBlank { "0" }}"
        )
    }

    private fun loadVehicles() {
        if (!isAdded) return

        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                repository.getVehicles(searchQuery, statusFilter, sortOrder)
            }.onSuccess { vehicles ->
                adapter.updateList(vehicles)
            }.onFailure { error ->
                Log.e(TAG, "Failed to load vehicles", error)
                adapter.updateList(emptyList())
                if (isAdded) {
                    Toast.makeText(requireContext(), R.string.vehicle_load_failed, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

