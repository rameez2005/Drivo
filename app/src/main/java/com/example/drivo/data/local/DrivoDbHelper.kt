package com.example.drivo.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.drivo.models.Vehicle

class DrivoDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_VEHICLES (
                $COL_VEHICLE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_REGISTRATION TEXT NOT NULL UNIQUE,
                $COL_MAKE TEXT NOT NULL,
                $COL_MODEL TEXT NOT NULL,
                $COL_YEAR INTEGER NOT NULL,
                $COL_TYPE TEXT NOT NULL,
                $COL_STATUS TEXT NOT NULL,
                $COL_DRIVER TEXT NOT NULL,
                $COL_ROUTE TEXT NOT NULL,
                $COL_LAST_MAINTENANCE TEXT NOT NULL,
                $COL_LAST_COST REAL NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_MAINTENANCE (
                $COL_MAINTENANCE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_MAINTENANCE_VEHICLE_ID INTEGER NOT NULL,
                $COL_PART_NAME TEXT NOT NULL,
                $COL_ACTION TEXT NOT NULL,
                $COL_SERVICE_DATE TEXT NOT NULL,
                $COL_COST REAL NOT NULL,
                FOREIGN KEY($COL_MAINTENANCE_VEHICLE_ID) REFERENCES $TABLE_VEHICLES($COL_VEHICLE_ID)
                    ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MAINTENANCE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VEHICLES")
        onCreate(db)
    }

    fun getVehicleCount(): Int {
        val query = "SELECT COUNT(*) FROM $TABLE_VEHICLES"
        readableDatabase.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(0)
            }
        }
        return 0
    }

    fun insertVehicle(vehicle: Vehicle): Long {
        val values = ContentValues().apply {
            put(COL_REGISTRATION, vehicle.registrationNumber)
            put(COL_MAKE, vehicle.make)
            put(COL_MODEL, vehicle.model)
            put(COL_YEAR, vehicle.year)
            put(COL_TYPE, vehicle.vehicleType)
            put(COL_STATUS, vehicle.status)
            put(COL_DRIVER, vehicle.assignedDriver)
            put(COL_ROUTE, vehicle.assignedRoute)
            put(COL_LAST_MAINTENANCE, vehicle.lastMaintenance)
            put(COL_LAST_COST, parseCurrency(vehicle.maintenanceCost))
        }
        return writableDatabase.insert(TABLE_VEHICLES, null, values)
    }

    fun updateVehicle(vehicleId: Int, vehicle: Vehicle): Int {
        val values = ContentValues().apply {
            put(COL_REGISTRATION, vehicle.registrationNumber)
            put(COL_MAKE, vehicle.make)
            put(COL_MODEL, vehicle.model)
            put(COL_YEAR, vehicle.year)
            put(COL_TYPE, vehicle.vehicleType)
            put(COL_STATUS, vehicle.status)
            put(COL_DRIVER, vehicle.assignedDriver)
            put(COL_ROUTE, vehicle.assignedRoute)
            put(COL_LAST_MAINTENANCE, vehicle.lastMaintenance)
            put(COL_LAST_COST, parseCurrency(vehicle.maintenanceCost))
        }
        return writableDatabase.update(
            TABLE_VEHICLES,
            values,
            "$COL_VEHICLE_ID = ?",
            arrayOf(vehicleId.toString())
        )
    }

    fun deleteVehicle(vehicleId: Int): Int {
        return writableDatabase.delete(
            TABLE_VEHICLES,
            "$COL_VEHICLE_ID = ?",
            arrayOf(vehicleId.toString())
        )
    }

    fun queryVehicles(
        searchQuery: String,
        statusFilter: String,
        sortOrder: String
    ): List<Vehicle> {
        val whereClauses = mutableListOf<String>()
        val args = mutableListOf<String>()

        if (searchQuery.isNotBlank()) {
            whereClauses += "($COL_REGISTRATION LIKE ? OR $COL_MAKE LIKE ? OR $COL_MODEL LIKE ? OR $COL_DRIVER LIKE ?)"
            repeat(4) { args += "%$searchQuery%" }
        }

        if (statusFilter != FILTER_ALL) {
            whereClauses += "$COL_STATUS = ?"
            args += statusFilter
        }

        val whereSql = if (whereClauses.isEmpty()) "" else "WHERE ${whereClauses.joinToString(" AND ")}" 
        val orderSql = when (sortOrder) {
            SORT_YEAR_DESC -> "$COL_YEAR DESC"
            SORT_MAINTENANCE_DESC -> "$COL_LAST_COST DESC"
            SORT_REG_ASC -> "$COL_REGISTRATION ASC"
            else -> "$COL_VEHICLE_ID DESC"
        }

        val sql = "SELECT * FROM $TABLE_VEHICLES $whereSql ORDER BY $orderSql"
        val vehicles = mutableListOf<Vehicle>()

        readableDatabase.rawQuery(sql, args.toTypedArray()).use { cursor ->
            while (cursor.moveToNext()) {
                vehicles += mapVehicle(cursor)
            }
        }

        return vehicles
    }

    fun getVehicleById(vehicleId: Int): Vehicle? {
        val sql = "SELECT * FROM $TABLE_VEHICLES WHERE $COL_VEHICLE_ID = ?"
        readableDatabase.rawQuery(sql, arrayOf(vehicleId.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                return mapVehicle(cursor)
            }
        }
        return null
    }

    fun insertMaintenanceLog(log: MaintenanceLog): Long {
        val values = ContentValues().apply {
            put(COL_MAINTENANCE_VEHICLE_ID, log.vehicleId)
            put(COL_PART_NAME, log.partName)
            put(COL_ACTION, log.actionTaken)
            put(COL_SERVICE_DATE, log.serviceDate)
            put(COL_COST, log.cost)
        }
        val id = writableDatabase.insert(TABLE_MAINTENANCE, null, values)

        if (id > 0) {
            val vehicleValues = ContentValues().apply {
                put(COL_LAST_MAINTENANCE, log.serviceDate)
                put(COL_LAST_COST, log.cost)
            }
            writableDatabase.update(
                TABLE_VEHICLES,
                vehicleValues,
                "$COL_VEHICLE_ID = ?",
                arrayOf(log.vehicleId.toString())
            )
        }
        return id
    }

    fun getMaintenanceLogs(vehicleId: Int): List<MaintenanceLog> {
        val logs = mutableListOf<MaintenanceLog>()
        val sql =
            "SELECT * FROM $TABLE_MAINTENANCE WHERE $COL_MAINTENANCE_VEHICLE_ID = ? ORDER BY $COL_MAINTENANCE_ID DESC"

        readableDatabase.rawQuery(sql, arrayOf(vehicleId.toString())).use { cursor ->
            while (cursor.moveToNext()) {
                logs += MaintenanceLog(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MAINTENANCE_ID)),
                    vehicleId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MAINTENANCE_VEHICLE_ID)),
                    partName = cursor.getString(cursor.getColumnIndexOrThrow(COL_PART_NAME)),
                    actionTaken = cursor.getString(cursor.getColumnIndexOrThrow(COL_ACTION)),
                    serviceDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_SERVICE_DATE)),
                    cost = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_COST))
                )
            }
        }
        return logs
    }

    private fun mapVehicle(cursor: android.database.Cursor): Vehicle {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_VEHICLE_ID))
        val cost = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LAST_COST))

        return Vehicle(
            vehicleId = id.toString(),
            registrationNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_REGISTRATION)),
            make = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAKE)),
            model = cursor.getString(cursor.getColumnIndexOrThrow(COL_MODEL)),
            year = cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR)),
            vehicleType = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
            status = cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)),
            assignedDriver = cursor.getString(cursor.getColumnIndexOrThrow(COL_DRIVER)),
            assignedRoute = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROUTE)),
            lastMaintenance = cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_MAINTENANCE)),
            maintenanceCost = "PKR ${"%.0f".format(cost)}"
        )
    }

    private fun parseCurrency(costText: String): Double {
        return costText.replace("PKR", "")
            .replace(",", "")
            .trim()
            .toDoubleOrNull()
            ?: 0.0
    }

    companion object {
        private const val DATABASE_NAME = "drivo.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_VEHICLES = "vehicles"
        const val COL_VEHICLE_ID = "id"
        const val COL_REGISTRATION = "registration_number"
        const val COL_MAKE = "make"
        const val COL_MODEL = "model"
        const val COL_YEAR = "year"
        const val COL_TYPE = "vehicle_type"
        const val COL_STATUS = "status"
        const val COL_DRIVER = "assigned_driver"
        const val COL_ROUTE = "assigned_route"
        const val COL_LAST_MAINTENANCE = "last_maintenance"
        const val COL_LAST_COST = "last_cost"

        const val TABLE_MAINTENANCE = "maintenance_logs"
        const val COL_MAINTENANCE_ID = "id"
        const val COL_MAINTENANCE_VEHICLE_ID = "vehicle_id"
        const val COL_PART_NAME = "part_name"
        const val COL_ACTION = "action_taken"
        const val COL_SERVICE_DATE = "service_date"
        const val COL_COST = "cost"

        const val FILTER_ALL = "ALL"
        const val SORT_RECENT = "RECENT"
        const val SORT_YEAR_DESC = "YEAR_DESC"
        const val SORT_MAINTENANCE_DESC = "MAINTENANCE_DESC"
        const val SORT_REG_ASC = "REG_ASC"
    }
}

