package com.example.drivo.models

import java.io.Serializable

data class Driver(
    val driverId: String,
    val fullName: String,
    val phone: String,
    val licenseNumber: String,
    val licenseExpiry: String,     // Display string e.g. "Dec 2026"
    val assignedVehicle: String,   // Registration number string
    val assignedRoute: String,
    val status: String,            // "ACTIVE", "ON_LEAVE"
    val availabilityStatus: String,// "AVAILABLE", "UNAVAILABLE", "ON_ROUTE"
    val attendanceDays: Int,       // e.g. 24
    val totalWorkingDays: Int,     // e.g. 26
    val pendingDues: String,       // Display string e.g. "PKR 3,500"
    val performanceRating: String  // "A", "B", "C", "D"
) : Serializable

