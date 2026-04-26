package com.example.drivo.data.local

import java.io.Serializable

data class MaintenanceLog(
    val id: Int = 0,
    val vehicleId: Int,
    val partName: String,
    val actionTaken: String,
    val serviceDate: String,
    val cost: Double
) : Serializable

