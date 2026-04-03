package com.example.drivo.models

import java.io.Serializable

data class Vehicle(
    val vehicleId: String,
    val registrationNumber: String,
    val make: String,
    val model: String,
    val year: Int,
    val vehicleType: String,       // "BUS", "MINIBUS", "COASTER", "VAN"
    val status: String,            // "ACTIVE", "MAINTENANCE", "RETIRED"
    val assignedDriver: String,    // Driver name string
    val assignedRoute: String,
    val lastMaintenance: String,   // Display string e.g. "15 Mar 2025"
    val maintenanceCost: String    // Display string e.g. "PKR 2,500"
) : Serializable

