package com.example.drivo.data.repository

import android.content.Context
import com.example.drivo.data.local.DrivoDbHelper
import com.example.drivo.data.local.MaintenanceLog
import com.example.drivo.models.DataSource
import com.example.drivo.models.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VehicleRepository(context: Context) {

    private val dbHelper = DrivoDbHelper(context.applicationContext)

    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        if (dbHelper.getVehicleCount() == 0) {
            DataSource.getVehicles().forEach { vehicle ->
                val id = dbHelper.insertVehicle(vehicle).toInt()
                if (id > 0) {
                    dbHelper.insertMaintenanceLog(
                        MaintenanceLog(
                            vehicleId = id,
                            partName = "Engine Oil",
                            actionTaken = "Replaced",
                            serviceDate = vehicle.lastMaintenance,
                            cost = vehicle.maintenanceCost.replace("PKR", "").replace(",", "").trim().toDoubleOrNull()
                                ?: 0.0
                        )
                    )
                }
            }
        }
    }

    suspend fun getVehicles(searchQuery: String, statusFilter: String, sortOrder: String): List<Vehicle> =
        withContext(Dispatchers.IO) {
            dbHelper.queryVehicles(searchQuery, statusFilter, sortOrder)
        }

    suspend fun addVehicle(vehicle: Vehicle): Long = withContext(Dispatchers.IO) {
        dbHelper.insertVehicle(vehicle)
    }

    suspend fun updateVehicle(vehicleId: Int, vehicle: Vehicle): Int = withContext(Dispatchers.IO) {
        dbHelper.updateVehicle(vehicleId, vehicle)
    }

    suspend fun deleteVehicle(vehicleId: Int): Int = withContext(Dispatchers.IO) {
        dbHelper.deleteVehicle(vehicleId)
    }

    suspend fun getVehicle(vehicleId: Int): Vehicle? = withContext(Dispatchers.IO) {
        dbHelper.getVehicleById(vehicleId)
    }

    suspend fun addMaintenanceLog(log: MaintenanceLog): Long = withContext(Dispatchers.IO) {
        dbHelper.insertMaintenanceLog(log)
    }

    suspend fun getMaintenanceLogs(vehicleId: Int): List<MaintenanceLog> = withContext(Dispatchers.IO) {
        dbHelper.getMaintenanceLogs(vehicleId)
    }
}

