# Assignment 3 - Quick Implementation Guide

## What Assignment 3 Adds to Assignment 2

Assignment 2: Static UI layouts (frontend only)
Assignment 3: Functional app with navigation, data passing, RecyclerView

## Data Models

### Vehicle.kt
```kotlin
data class Vehicle(
    val vehicleId: String,
    val registrationNumber: String,     // "LEA-1234"
    val make: String,                   // "Toyota"
    val model: String,                  // "Coaster"
    val year: Int,                      // 2019
    val vehicleType: String,            // "COASTER", "BUS", "VAN"
    val status: String,                 // "ACTIVE", "MAINTENANCE", "RETIRED"
    val assignedDriver: String,         // "Ali Hassan"
    val assignedRoute: String,          // "Lahore → Shahdara"
    val lastMaintenance: String,        // "15 Mar 2025"
    val maintenanceCost: String         // "PKR 2,500"
) : Serializable
```

### Driver.kt
```kotlin
data class Driver(
    val driverId: String,
    val fullName: String,
    val phone: String,
    val licenseNumber: String,
    val licenseExpiry: String,
    val assignedVehicle: String,
    val assignedRoute: String,
    val status: String,                 // "ACTIVE", "ON_LEAVE"
    val availabilityStatus: String,     // "AVAILABLE", "UNAVAILABLE", "ON_ROUTE"
    val attendanceDays: Int,            // 24
    val totalWorkingDays: Int,          // 26
    val pendingDues: String,            // "PKR 3,500"
    val performanceRating: String       // "A", "B", "C", "D"
) : Serializable
```

## DataSource - Hardcoded Data

```kotlin
object DataSource {
    fun getVehicles(): List<Vehicle> = listOf(
        Vehicle("v1", "LEA-1234", "Toyota", "Coaster", 2019, "COASTER", 
                "ACTIVE", "Ali Hassan", "Lahore Industrial Estate → Shahdara", 
                "15 Mar 2025", "PKR 2,500"),
        // ... 7 more vehicles
    )
    
    fun getDrivers(): List<Driver> = listOf(
        Driver("d1", "Ali Hassan", "+92 300 1234567", "LHV-123456", "Dec 2026",
               "LEA-1234", "Lahore Industrial Estate → Shahdara", "ACTIVE", 
               "AVAILABLE", 24, 26, "PKR 0", "A"),
        // ... 7 more drivers
    )
}
```

## Navigation Flow

```
SplashActivity (2 second splash)
         ↓
   MainActivity (container for fragments)
    ↙    ↓    ↘
Dashboard  VehicleList  DriverList
    ↓         ↓             ↓
  VehicleDetail  DriverDetail
```

## Key Features (F1-F5)

### F1: Intent & Navigation
- SplashActivity delays 2 seconds
- Passes OWNER_NAME via Intent extra
- MainActivity receives and displays owner info

### F2: Bundle Data Passing
- RecyclerView item click → Bundle with object
- DetailFragment receives Bundle, displays data
- Both Vehicle and Driver use same pattern

### F3: RecyclerView Lists
- VehicleAdapter with custom ViewHolder
- DriverAdapter with custom ViewHolder
- Both implement search/filter via updateList()

### F4: Fragment Transactions
- Bottom navigation switches fragments
- Use replace() NOT add()
- Use addToBackStack() for back button support

### F5: Search & Filter
- SearchView.OnQueryTextListener for text search
- RadioGroup listener for status filter
- Call adapter.updateList() to refresh list

## Critical Code Patterns

### Fragment Transaction (F4)
```kotlin
supportFragmentManager.beginTransaction()
    .replace(R.id.fragment_container, newFragment)  // Replace old with new
    .addToBackStack(null)                           // Enable back button
    .commit()
```

### Bundle Data Passing (F2)
```kotlin
// Sending
val bundle = Bundle()
bundle.putSerializable("VEHICLE_DATA", vehicle)
detailFragment.arguments = bundle

// Receiving
val vehicle = arguments?.getSerializable("VEHICLE_DATA") as? Vehicle
```

### RecyclerView Adapter Click (F2 + F3)
```kotlin
holder.itemView.setOnClickListener {
    val bundle = Bundle()
    bundle.putSerializable("VEHICLE_DATA", vehicle)
    val detailFragment = VehicleDetailFragment()
    detailFragment.arguments = bundle
    (context as MainActivity).loadFragment(detailFragment)
}
```

### Search Implementation (F5)
```kotlin
searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
    override fun onQueryTextChange(newText: String?): Boolean {
        val filtered = allVehicles.filter {
            it.registrationNumber.contains(newText ?: "", ignoreCase = true)
        }
        adapter.updateList(filtered)
        return true
    }
    override fun onQueryTextSubmit(query: String?) = false
})
```

### Filter Implementation (F5)
```kotlin
radioGroup.setOnCheckedChangeListener { _, checkedId ->
    val filtered = when (checkedId) {
        R.id.rb_active -> allVehicles.filter { it.status == "ACTIVE" }
        R.id.rb_maintenance -> allVehicles.filter { it.status == "MAINTENANCE" }
        else -> allVehicles
    }
    adapter.updateList(filtered)
}
```

## Activity/Fragment Lifecycle

### SplashActivity
```
onCreate() → Handler.postDelayed(2s) → startActivity(Intent) → finish()
```

### MainActivity
```
onCreate() → load DashboardFragment → setupBottomNavigation()
           → listen for bottom nav clicks → loadFragment()
```

### DashboardFragment
```
onCreateView() → inflate layout
onViewCreated() → get owner name from Bundle → display in UI
```

### VehicleListFragment
```
onCreateView() → inflate layout
onViewCreated() → load all vehicles → setup adapter → setup search/filter
```

### VehicleDetailFragment
```
onViewCreated() → get Vehicle from Bundle → populate all fields
```

## Grading (F1-F5 = 100 points)

- **F1 (Intent):** 25 points - Splash → Main with extras
- **F2 (Bundle):** 20 points - Passing objects to details
- **F3 (RecyclerView):** 25 points - List + adapter + viewholder
- **F4 (Fragment):** 20 points - Bottom nav + back stack
- **F5 (Search):** Part of F3 (or 10 points separate)

## DO NOT Do

❌ Static variables for data passing
❌ SharedPreferences for passing data
❌ Multiple Activities for each screen
❌ ListView (use RecyclerView only)
❌ add() instead of replace() for fragments
❌ forget addToBackStack() - breaks back button
❌ Hardcode vehicle/driver data in fragments
❌ Forget `: Serializable` on data classes

## Bundle Key Names

Use these exact keys for consistency:
- `"VEHICLE_DATA"` - passes Vehicle object
- `"DRIVER_DATA"` - passes Driver object
- `"OWNER_NAME"` - passes owner name string
- `"COMPANY_NAME"` - passes company name string

## RecyclerView ViewHolder Pattern

```kotlin
class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvRegNumber: TextView = itemView.findViewById(R.id.tv_reg_number)
    val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
    val viewStatusDot: View = itemView.findViewById(R.id.view_status_dot)
    // ... more views
}
```

Benefits:
- findViewById() happens once per ViewHolder, not per bind
- Much faster scrolling
- Reused for all list items

## Adapter Methods to Know

- `onCreateViewHolder()` - inflate layout, create ViewHolder
- `onBindViewHolder()` - bind data to ViewHolder's views
- `getItemCount()` - return list size
- `updateList()` - replace data and notifyDataSetChanged()

## Status Dot Colors

Use ColorStateList for dynamic coloring:
```kotlin
val color = when (vehicle.status) {
    "ACTIVE" -> R.color.colorStatusActive       // #4CAF50 Green
    "MAINTENANCE" -> R.color.colorStatusMaintenance  // #FF9800 Orange
    else -> R.color.colorStatusRetired          // #F44336 Red
}
holder.viewStatusDot.backgroundTintList = 
    ColorStateList.valueOf(ContextCompat.getColor(context, color))
```

## GitHub Requirements

- One repo per group
- All members as collaborators
- Pull before starting work
- Commit messages: `feat: add VehicleAdapter`, `fix: bundle key typo`
- Don't commit: `.gradle/`, `build/`, `.idea/`, `local.properties`

