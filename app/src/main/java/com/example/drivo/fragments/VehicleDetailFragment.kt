package com.example.drivo.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.R
import com.example.drivo.adapters.MaintenanceLogAdapter
import com.example.drivo.data.local.MaintenanceLog
import com.example.drivo.data.repository.VehicleRepository
import com.example.drivo.models.Vehicle
import kotlinx.coroutines.launch

class VehicleDetailFragment : Fragment() {

    private lateinit var repository: VehicleRepository
    private lateinit var maintenanceAdapter: MaintenanceLogAdapter
    private var currentVehicle: Vehicle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = VehicleRepository(requireContext())
        maintenanceAdapter = MaintenanceLogAdapter()

        view.findViewById<RecyclerView>(R.id.recycler_maintenance_logs).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = maintenanceAdapter
        }

        currentVehicle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("VEHICLE_DATA", Vehicle::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable("VEHICLE_DATA") as? Vehicle
        }
        bindVehicle(currentVehicle)

        view.findViewById<Button>(R.id.btn_edit_vehicle).setOnClickListener {
            currentVehicle?.let { vehicle ->
                showVehicleFormDialog(vehicle)
            }
        }

        view.findViewById<Button>(R.id.btn_delete_vehicle).setOnClickListener {
            currentVehicle?.let { vehicle ->
                confirmDelete(vehicle)
            }
        }

        view.findViewById<Button>(R.id.btn_add_maintenance).setOnClickListener {
            currentVehicle?.let { vehicle ->
                showMaintenanceDialog(vehicle)
            }
        }

        loadMaintenanceLogs()
    }

    private fun bindVehicle(vehicle: Vehicle?) {
        vehicle ?: return
        val root = view ?: return

        root.findViewById<TextView>(R.id.tv_reg_number).text = vehicle.registrationNumber
        root.findViewById<TextView>(R.id.tv_make_model).text = "${vehicle.make} ${vehicle.model} ${vehicle.year}"
        root.findViewById<TextView>(R.id.tv_status).text = vehicle.status
        root.findViewById<TextView>(R.id.tv_driver).text = vehicle.assignedDriver
        root.findViewById<TextView>(R.id.tv_route).text = vehicle.assignedRoute
        root.findViewById<TextView>(R.id.tv_last_maintenance).text = vehicle.lastMaintenance
        root.findViewById<TextView>(R.id.tv_maintenance_cost).text = vehicle.maintenanceCost
    }

    private fun showVehicleFormDialog(vehicle: Vehicle) {
        val formView = layoutInflater.inflate(R.layout.dialog_vehicle_form, null)

        formView.findViewById<EditText>(R.id.et_registration).setText(vehicle.registrationNumber)
        formView.findViewById<EditText>(R.id.et_make).setText(vehicle.make)
        formView.findViewById<EditText>(R.id.et_model).setText(vehicle.model)
        formView.findViewById<EditText>(R.id.et_year).setText(vehicle.year.toString())
        formView.findViewById<EditText>(R.id.et_type).setText(vehicle.vehicleType)
        formView.findViewById<EditText>(R.id.et_status).setText(vehicle.status)
        formView.findViewById<EditText>(R.id.et_driver).setText(vehicle.assignedDriver)
        formView.findViewById<EditText>(R.id.et_route).setText(vehicle.assignedRoute)
        formView.findViewById<EditText>(R.id.et_last_maintenance).setText(vehicle.lastMaintenance)
        formView.findViewById<EditText>(R.id.et_maintenance_cost)
            .setText(vehicle.maintenanceCost.replace("PKR", "").replace(",", "").trim())

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.edit_vehicle))
            .setView(formView)
            .setPositiveButton(getString(R.string.update)) { _, _ ->
                val updatedVehicle = buildVehicleFromForm(formView, vehicle) ?: return@setPositiveButton
                val vehicleId = vehicle.vehicleId.toIntOrNull() ?: return@setPositiveButton

                viewLifecycleOwner.lifecycleScope.launch {
                    repository.updateVehicle(vehicleId, updatedVehicle)
                    currentVehicle = repository.getVehicle(vehicleId)
                    bindVehicle(currentVehicle)
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun confirmDelete(vehicle: Vehicle) {
        val vehicleId = vehicle.vehicleId.toIntOrNull() ?: return

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_vehicle))
            .setMessage(getString(R.string.delete_vehicle_confirmation, vehicle.registrationNumber))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.deleteVehicle(vehicleId)
                    parentFragmentManager.popBackStack()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showMaintenanceDialog(vehicle: Vehicle) {
        val formView = layoutInflater.inflate(R.layout.dialog_maintenance_form, null)
        val vehicleId = vehicle.vehicleId.toIntOrNull() ?: return

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_maintenance_log))
            .setView(formView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val partName = formView.findViewById<EditText>(R.id.et_part_name).text.toString().trim()
                val action = formView.findViewById<EditText>(R.id.et_action).text.toString().trim()
                val date = formView.findViewById<EditText>(R.id.et_service_date).text.toString().trim()
                val cost = formView.findViewById<EditText>(R.id.et_cost).text.toString().toDoubleOrNull() ?: 0.0

                if (partName.isBlank() || action.isBlank() || date.isBlank()) return@setPositiveButton

                viewLifecycleOwner.lifecycleScope.launch {
                    repository.addMaintenanceLog(
                        MaintenanceLog(
                            vehicleId = vehicleId,
                            partName = partName,
                            actionTaken = action,
                            serviceDate = date,
                            cost = cost
                        )
                    )

                    currentVehicle = repository.getVehicle(vehicleId)
                    bindVehicle(currentVehicle)
                    loadMaintenanceLogs()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun loadMaintenanceLogs() {
        val vehicleId = currentVehicle?.vehicleId?.toIntOrNull() ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            val logs = repository.getMaintenanceLogs(vehicleId)
            maintenanceAdapter.submitList(logs)
        }
    }

    private fun buildVehicleFromForm(formView: View, existingVehicle: Vehicle): Vehicle? {
        val registration = formView.findViewById<EditText>(R.id.et_registration).text.toString().trim()
        val make = formView.findViewById<EditText>(R.id.et_make).text.toString().trim()
        val model = formView.findViewById<EditText>(R.id.et_model).text.toString().trim()
        val year = formView.findViewById<EditText>(R.id.et_year).text.toString().trim().toIntOrNull() ?: return null
        val type = formView.findViewById<EditText>(R.id.et_type).text.toString().trim()
        val status = formView.findViewById<EditText>(R.id.et_status).text.toString().trim().uppercase()
        val driver = formView.findViewById<EditText>(R.id.et_driver).text.toString().trim()
        val route = formView.findViewById<EditText>(R.id.et_route).text.toString().trim()
        val lastMaintenance = formView.findViewById<EditText>(R.id.et_last_maintenance).text.toString().trim()
        val maintenanceCost = formView.findViewById<EditText>(R.id.et_maintenance_cost).text.toString().trim()

        return Vehicle(
            vehicleId = existingVehicle.vehicleId,
            registrationNumber = registration,
            make = make,
            model = model,
            year = year,
            vehicleType = type,
            status = status,
            assignedDriver = driver,
            assignedRoute = route,
            lastMaintenance = lastMaintenance,
            maintenanceCost = "PKR ${maintenanceCost.ifBlank { "0" }}"
        )
    }
}

