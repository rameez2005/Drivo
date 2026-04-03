package com.example.drivo.models

object DataSource {

    fun getVehicles(): List<Vehicle> = listOf(
        Vehicle("v1", "LEA-1234", "Toyota", "Coaster", 2019, "COASTER", "ACTIVE", "Ali Hassan", "Lahore Industrial Estate → Shahdara", "15 Mar 2025", "PKR 2,500"),
        Vehicle("v2", "LEB-5678", "Hino", "Bus", 2017, "BUS", "ACTIVE", "Usman Tariq", "SITE Karachi → Korangi", "02 Apr 2025", "PKR 4,800"),
        Vehicle("v3", "LHR-4321", "Toyota", "Hi-Ace", 2020, "VAN", "MAINTENANCE", "Bilal Ahmed", "Faisalabad Textile Mill → City Center", "28 Feb 2025", "PKR 8,500"),
        Vehicle("v4", "ISB-9900", "Toyota", "Coaster", 2018, "COASTER", "ACTIVE", "Kamran Sheikh", "Islamabad Industrial Zone → G-9", "10 Jan 2025", "PKR 1,200"),
        Vehicle("v5", "KHI-0011", "Daewoo", "Bus", 2015, "BUS", "RETIRED", "Unassigned", "No Route", "05 Dec 2024", "PKR 18,000"),
        Vehicle("v6", "LHR-7777", "Hino", "Minibus", 2021, "MINIBUS", "ACTIVE", "Farhan Malik", "Multan Road Industrial → Township", "20 Mar 2025", "PKR 3,100"),
        Vehicle("v7", "FSD-2233", "Toyota", "Coaster", 2016, "COASTER", "MAINTENANCE", "Unassigned", "Under Repair", "01 Apr 2025", "PKR 12,000"),
        Vehicle("v8", "KHI-4455", "Mercedes", "Bus", 2022, "BUS", "ACTIVE", "Shahid Raza", "Korangi Industrial → Saddar", "12 Mar 2025", "PKR 5,500")
    )

    fun getDrivers(): List<Driver> = listOf(
        Driver("d1", "Ali Hassan", "+92 300 1234567", "LHV-123456", "Dec 2026", "LEA-1234", "Lahore Industrial Estate → Shahdara", "ACTIVE", "AVAILABLE", 24, 26, "PKR 0", "A"),
        Driver("d2", "Usman Tariq", "+92 321 9876543", "LHV-234567", "Mar 2025", "LEB-5678", "SITE Karachi → Korangi", "ACTIVE", "ON_ROUTE", 22, 26, "PKR 3,500", "B"),
        Driver("d3", "Bilal Ahmed", "+92 333 5551234", "LHV-345678", "Aug 2026", "LHR-4321", "Faisalabad Textile Mill → City Center", "ACTIVE", "UNAVAILABLE", 20, 26, "PKR 1,200", "B"),
        Driver("d4", "Kamran Sheikh", "+92 345 7890123", "LHV-456789", "Jun 2027", "ISB-9900", "Islamabad Industrial Zone → G-9", "ACTIVE", "AVAILABLE", 26, 26, "PKR 0", "A"),
        Driver("d5", "Farhan Malik", "+92 311 1112222", "LHV-567890", "Jan 2026", "LHR-7777", "Multan Road Industrial → Township", "ACTIVE", "ON_ROUTE", 18, 26, "PKR 5,000", "C"),
        Driver("d6", "Shahid Raza", "+92 322 3334444", "LHV-678901", "Sep 2025", "KHI-4455", "Korangi Industrial → Saddar", "ACTIVE", "AVAILABLE", 25, 26, "PKR 800", "A"),
        Driver("d7", "Imran Butt", "+92 301 5556666", "LHV-789012", "Feb 2026", "Unassigned", "No Route", "ON_LEAVE", "UNAVAILABLE", 10, 26, "PKR 2,000", "D"),
        Driver("d8", "Zubair Khan", "+92 312 7778888", "LHV-890123", "Nov 2026", "Unassigned", "No Route", "ACTIVE", "AVAILABLE", 23, 26, "PKR 0", "B")
    )
}

