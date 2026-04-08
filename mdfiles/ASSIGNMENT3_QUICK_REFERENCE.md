# Assignment 3 - Implementation Notes & Quick Reference

## CRITICAL IMPORTS TO ADD

Make sure these imports are present in your project's `build.gradle.kts`:

```gradle
dependencies {
    // AndroidX
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // Material Design
    implementation("com.google.android.material:material:1.11.0")
    
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
}
```

---

## ACTIVITY/FRAGMENT LIFECYCLE QUICK REFERENCE

### SplashActivity
```
onCreate() 
  → setContentView(activity_splash.xml)
  → Handler.postDelayed(2000)
    → Intent to MainActivity with extras
    → startActivity(intent)
    → finish()
```

### MainActivity
```
onCreate()
  → setContentView(activity_main.xml)
  → Get Intent extras (OWNER_NAME, COMPANY_NAME)
  → Create Bundle with extras
  → Create DashboardFragment
  → Set fragment.arguments = bundle
  → FragmentManager.replace(DashboardFragment)
  → setupBottomNavigation()
    → Listen for bottom nav item selection
    → loadFragment(selectedFragment)
```

### Fragments
```
onCreateView()
  → Inflate layout XML

onViewCreated()
  → Get Bundle from arguments
  → Find views by ID
  → Set data to views
  → Attach listeners (SearchView, etc.)
```

---

## FRAGMENT TRANSACTIONS PATTERN

**WRONG - Uses add() - MEMORY LEAK:**
```kotlin
supportFragmentManager.beginTransaction()
    .add(R.id.fragment_container, newFragment)  // ❌ WRONG
    .commit()
```

**CORRECT - Uses replace():**
```kotlin
supportFragmentManager.beginTransaction()
    .replace(R.id.fragment_container, newFragment)  // ✅ RIGHT
    .addToBackStack(null)  // ✅ Enable back button
    .commit()
```

---

## BUNDLE DATA PASSING PATTERN

**Sending:**
```kotlin
val bundle = Bundle()
bundle.putSerializable("VEHICLE_DATA", vehicle)  // Vehicle must implement Serializable
fragment.arguments = bundle

loadFragment(fragment)
```

**Receiving:**
```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val vehicle = arguments?.getSerializable("VEHICLE_DATA") as? Vehicle
    vehicle?.let {
        // Populate views with vehicle data
    }
}
```

**Safe approach - always use `as?` (safe cast):**
```kotlin
val vehicle = arguments?.getSerializable("VEHICLE_DATA") as? Vehicle
// If it's not a Vehicle, vehicle will be null (no crash)
```

---

## RECYCLERVIEW ADAPTER PATTERN

```kotlin
class VehicleAdapter(
    private val context: Context,
    private var vehicleList: List<Vehicle>
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    // 1. ViewHolder caches views
    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRegNumber: TextView = itemView.findViewById(R.id.tv_reg_number)
        // ... more views
    }

    // 2. Create ViewHolder when needed (not every bind)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_vehicle_card, parent, false)
        return VehicleViewHolder(view)
    }

    // 3. Bind data to ViewHolder (reuses same ViewHolder)
    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]
        holder.tvRegNumber.text = vehicle.registrationNumber
        // ... set more data
        
        // Item click listener
        holder.itemView.setOnClickListener {
            // Pass vehicle to detail fragment
        }
    }

    // 4. Tell RecyclerView how many items
    override fun getItemCount() = vehicleList.size

    // 5. Called by search/filter
    fun updateList(newList: List<Vehicle>) {
        vehicleList = newList
        notifyDataSetChanged()  // Refresh entire list
    }
}
```

---

## SEARCH & FILTER PATTERN

```kotlin
// SearchView for text search
val searchView = view.findViewById<SearchView>(R.id.search_view)
searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?) = false
    
    override fun onQueryTextChange(newText: String?): Boolean {
        filterVehicles(newText ?: "")
        return true
    }
})

fun filterVehicles(query: String) {
    val filtered = allVehicles.filter {
        it.registrationNumber.contains(query, ignoreCase = true) ||
        it.make.contains(query, ignoreCase = true)
    }
    adapter.updateList(filtered)  // Calls notifyDataSetChanged()
}

// RadioGroup for status filter
val radioGroup = view.findViewById<RadioGroup>(R.id.radio_filter)
radioGroup.setOnCheckedChangeListener { _, checkedId ->
    val filtered = when (checkedId) {
        R.id.rb_active -> allVehicles.filter { it.status == "ACTIVE" }
        R.id.rb_maintenance -> allVehicles.filter { it.status == "MAINTENANCE" }
        else -> allVehicles
    }
    adapter.updateList(filtered)
}
```

---

## STATUS DOT COLOR MAPPING

```kotlin
val dotColor = when (vehicle.status) {
    "ACTIVE" -> R.color.colorStatusActive          // #4CAF50 Green
    "MAINTENANCE" -> R.color.colorStatusMaintenance // #FF9800 Orange
    else -> R.color.colorStatusRetired              // #F44336 Red
}

holder.viewStatusDot.backgroundTintList =
    ColorStateList.valueOf(ContextCompat.getColor(context, dotColor))
```

---

## BOTTOM NAVIGATION SETUP

**activity_main.xml:**
```xml
<LinearLayout orientation="vertical">
    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />
    
    <BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu" />
</LinearLayout>
```

**MainActivity.kt:**
```kotlin
val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
bottomNav.setOnItemSelectedListener { item ->
    when (item.itemId) {
        R.id.nav_dashboard -> loadFragment(DashboardFragment())
        R.id.nav_vehicles -> loadFragment(VehicleListFragment())
        R.id.nav_drivers -> loadFragment(DriverListFragment())
    }
    true
}
```

---

## DATASOURCE USAGE

```kotlin
// In any Fragment:
val vehicles = DataSource.getVehicles()  // Returns List<Vehicle>
val drivers = DataSource.getDrivers()    // Returns List<Driver>

// Pass to Adapter
adapter = VehicleAdapter(requireContext(), vehicles)
recyclerView.adapter = adapter
```

---

## INTENT EXTRAS PATTERN

**Sender (SplashActivity):**
```kotlin
val intent = Intent(this, MainActivity::class.java)
intent.putExtra("OWNER_NAME", "Ahmed Khan")
intent.putExtra("COMPANY_NAME", "TransFleet Co.")
startActivity(intent)
```

**Receiver (MainActivity):**
```kotlin
val ownerName = intent.getStringExtra("OWNER_NAME") ?: "Owner"
val companyName = intent.getStringExtra("COMPANY_NAME") ?: "TransFleet"

val bundle = Bundle()
bundle.putString("OWNER_NAME", ownerName)
bundle.putString("COMPANY_NAME", companyName)
dashboardFragment.arguments = bundle
```

**Fragment (DashboardFragment):**
```kotlin
val ownerName = arguments?.getString("OWNER_NAME") ?: "Owner"
val companyName = arguments?.getString("COMPANY_NAME") ?: "TransFleet"

toolbar.title = companyName
toolbar.subtitle = "Welcome, $ownerName"
```

---

## COMMON MISTAKES TO AVOID

❌ **Using add() instead of replace():**
```kotlin
.add(R.id.fragment_container, fragment)  // WRONG - stacks fragments
.replace(R.id.fragment_container, fragment)  // CORRECT
```

❌ **Forgetting addToBackStack():**
```kotlin
supportFragmentManager.beginTransaction()
    .replace(R.id.fragment_container, fragment)
    .commit()  // WRONG - back button exits app

.addToBackStack(null)  // CORRECT - back button goes to previous fragment
.commit()
```

❌ **Using static variables for data passing:**
```kotlin
companion object {
    var sharedVehicle: Vehicle? = null  // WRONG - memory leak, hard to test
}

// Use Bundle instead
val bundle = Bundle()
bundle.putSerializable("VEHICLE", vehicle)  // CORRECT
```

❌ **Using notifyItemChanged() for search:**
```kotlin
adapter.notifyItemChanged(0)  // WRONG - doesn't refresh entire list

adapter.updateList(filtered)  // CORRECT
adapter.notifyDataSetChanged()  // Refreshes entire list
```

❌ **Forgetting Vehicle : Serializable:**
```kotlin
data class Vehicle(...)  // WRONG - can't pass via Bundle

data class Vehicle(...) : Serializable  // CORRECT
```

❌ **Not using safe cast for Bundle data:**
```kotlin
val vehicle = arguments?.getSerializable("VEHICLE_DATA") as Vehicle  // WRONG - crashes if wrong type

val vehicle = arguments?.getSerializable("VEHICLE_DATA") as? Vehicle  // CORRECT
```

---

## TESTING CHECKLIST

- [ ] SplashActivity shows for 2 seconds then navigates to MainActivity
- [ ] MainActivity receives and displays owner name
- [ ] Bottom navigation shows 3 tabs: Dashboard, Fleet, Drivers
- [ ] Clicking bottom tabs switches fragments (no activity restart)
- [ ] Back button works in detail screens
- [ ] Vehicle list shows all vehicles in RecyclerView
- [ ] Clicking vehicle opens VehicleDetailFragment with correct data
- [ ] Vehicle search filters by registration/make/model/driver
- [ ] Vehicle filter chips work (All/Active/Maintenance/Retired)
- [ ] Driver list shows all drivers in RecyclerView
- [ ] Clicking driver opens DriverDetailFragment with correct data
- [ ] Driver search filters by name/phone/vehicle
- [ ] Status dots are correct color (green/orange/red)
- [ ] Detail screens populate all fields from Bundle data
- [ ] No crashes on orientation change

---

## DEBUG TIPS

**Check Logcat for:**
```
"VEHICLE_DATA" // If Bundle key is wrong
ClassCastException  // If data type mismatch (use as?)
NullPointerException  // If view not found (check ID)
```

**Add debug Log statements:**
```kotlin
Log.d("VehicleAdapter", "Binding vehicle: ${vehicle.registrationNumber}")
Log.d("FragmentDetail", "Received vehicle from Bundle")
```

**Check AndroidManifest.xml:**
```xml
<!-- SplashActivity should be launcher -->
<action android:name="android.intent.action.MAIN" />
<category android:name="android.intent.category.LAUNCHER" />

<!-- MainActivity and DetailActivity should NOT have MAIN/LAUNCHER -->
```

---

## DEPLOYMENT CHECKLIST

Before submission:
- [ ] All files in correct packages (activities/, fragments/, adapters/, models/)
- [ ] AndroidManifest.xml updated (SplashActivity as launcher)
- [ ] All Fragments created and named correctly
- [ ] All Adapters created with updateList()
- [ ] DataSource has 8 vehicles and 8 drivers
- [ ] Bundle keys match (VEHICLE_DATA, DRIVER_DATA)
- [ ] Fragment replace() used, not add()
- [ ] addToBackStack(null) on all fragment transactions
- [ ] SearchView and RadioGroup filters working
- [ ] No compilation errors
- [ ] No runtime crashes

---

**Last Updated:** April 3, 2026
**Assignment 3 Status:** COMPLETE ✅

