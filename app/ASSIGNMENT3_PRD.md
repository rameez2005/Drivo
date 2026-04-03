# ASSIGNMENT3_PRD.md
# TransFleet Manager — Assignment 3: Static to Functional
# AI Agent Reference Document — READ THIS ENTIRE FILE BEFORE WRITING ANY CODE

---

## WHAT THIS ASSIGNMENT IS

Take the static XML screens from Assignment 2 and convert them into a **fully functional Android app** with:
- Real navigation between screens
- RecyclerView with real data
- Fragments as primary UI containers
- Data passing between screens via Intents and Bundles
- Search/filter on lists

---

## ABSOLUTE RULES — NEVER VIOLATE THESE

| Rule | Detail |
|------|--------|
| **Intents & Bundles ONLY for data passing** | No global static variables, no singleton classes, no SharedPreferences for passing data between screens |
| **Fragments are primary UI** | Activities are shells/containers only. All real UI content lives in Fragments |
| **RecyclerView for all lists** | No hardcoded repeated views, no ListView. Every list = RecyclerView + custom Adapter + ViewHolder |
| **Clean folder structure** | Must organize into packages: `activities/`, `fragments/`, `adapters/`, `models/` |
| **GitHub required** | Every member must commit. Commit messages must describe what changed |

---

## PROJECT FOLDER STRUCTURE — CREATE EXACTLY THIS

```
com/transfleet/manager/
├── activities/
│   ├── SplashActivity.kt
│   ├── MainActivity.kt
│   └── DetailActivity.kt
├── fragments/
│   ├── DashboardFragment.kt
│   ├── VehicleListFragment.kt
│   ├── DriverListFragment.kt
│   ├── VehicleDetailFragment.kt
│   └── DriverDetailFragment.kt
├── adapters/
│   ├── VehicleAdapter.kt
│   └── DriverAdapter.kt
└── models/
    ├── Vehicle.kt
    └── Driver.kt
```

---

## DATA MODELS — IMPLEMENT EXACTLY AS DEFINED

### `models/Vehicle.kt`
```kotlin
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
```

### `models/Driver.kt`
```kotlin
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
```

---

## DUMMY DATA — USE THIS EXACTLY (No backend, hardcode in a DataSource object)

### `models/DataSource.kt`
```kotlin
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
```

---

## FUNCTIONAL REQUIREMENTS — ALL 5 MUST BE IMPLEMENTED

---

### F1 — Intent: Splash → Main Navigation (25 marks shared with F4)

**Flow:**
1. App opens `SplashActivity`
2. After 2 seconds, navigate to `MainActivity` passing owner name via Intent Extra
3. `MainActivity` receives the name and displays it in the Dashboard toolbar

**SplashActivity.kt:**
```kotlin
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("OWNER_NAME", "Ahmed Khan")      // hardcoded owner name
            intent.putExtra("COMPANY_NAME", "TransFleet Co.")
            startActivity(intent)
            finish()
        }, 2000)
    }
}
```

**MainActivity.kt:**
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ownerName = intent.getStringExtra("OWNER_NAME") ?: "Owner"
        val companyName = intent.getStringExtra("COMPANY_NAME") ?: "TransFleet"

        // Pass to DashboardFragment via Bundle
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putString("OWNER_NAME", ownerName)
            bundle.putString("COMPANY_NAME", companyName)

            val dashboardFragment = DashboardFragment()
            dashboardFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, dashboardFragment)
                .commit()
        }

        // Set up bottom navigation
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> loadFragment(DashboardFragment())
                R.id.nav_vehicles -> loadFragment(VehicleListFragment())
                R.id.nav_drivers -> loadFragment(DriverListFragment())
            }
            true
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
```

---

### F2 — Bundle: RecyclerView Item → Detail Fragment (20 marks)

**When a vehicle card is tapped in the RecyclerView:**
1. The `Vehicle` object is put into a Bundle
2. `VehicleDetailFragment` is loaded with that Bundle
3. Detail screen reads the Bundle and displays full vehicle info

**In VehicleAdapter.kt — item click:**
```kotlin
holder.itemView.setOnClickListener {
    val bundle = Bundle()
    bundle.putSerializable("VEHICLE_DATA", vehicle)  // Vehicle implements Serializable

    val detailFragment = VehicleDetailFragment()
    detailFragment.arguments = bundle

    // Call back to activity to load fragment
    (context as MainActivity).loadFragment(detailFragment)
}
```

**In VehicleDetailFragment.kt — receive Bundle:**
```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val vehicle = arguments?.getSerializable("VEHICLE_DATA") as? Vehicle
    vehicle?.let {
        // Populate all views with vehicle data
        view.findViewById<TextView>(R.id.tv_reg_number).text = it.registrationNumber
        view.findViewById<TextView>(R.id.tv_make_model).text = "${it.make} ${it.model} ${it.year}"
        view.findViewById<TextView>(R.id.tv_status).text = it.status
        view.findViewById<TextView>(R.id.tv_driver).text = it.assignedDriver
        view.findViewById<TextView>(R.id.tv_route).text = it.assignedRoute
        view.findViewById<TextView>(R.id.tv_last_maintenance).text = it.lastMaintenance
        view.findViewById<TextView>(R.id.tv_maintenance_cost).text = it.maintenanceCost
    }
}
```

**Same pattern applies for Driver → DriverDetailFragment using Driver object.**

---

### F3 — RecyclerView: Vehicle List + Driver List (25 marks)

#### `adapters/VehicleAdapter.kt` — Full Implementation:
```kotlin
class VehicleAdapter(
    private val context: Context,
    private var vehicleList: List<Vehicle>
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRegNumber: TextView = itemView.findViewById(R.id.tv_reg_number)
        val tvMakeModel: TextView = itemView.findViewById(R.id.tv_make_model)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val tvDriver: TextView = itemView.findViewById(R.id.tv_driver)
        val tvRoute: TextView = itemView.findViewById(R.id.tv_route)
        val viewStatusDot: View = itemView.findViewById(R.id.view_status_dot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_vehicle_card, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]
        holder.tvRegNumber.text = vehicle.registrationNumber
        holder.tvMakeModel.text = "${vehicle.make} ${vehicle.model} (${vehicle.year})"
        holder.tvStatus.text = vehicle.status
        holder.tvDriver.text = vehicle.assignedDriver
        holder.tvRoute.text = vehicle.assignedRoute

        // Status dot color
        val dotColor = when (vehicle.status) {
            "ACTIVE" -> R.color.colorStatusActive
            "MAINTENANCE" -> R.color.colorStatusMaintenance
            else -> R.color.colorStatusRetired
        }
        holder.viewStatusDot.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, dotColor))

        // Item click — pass to detail
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("VEHICLE_DATA", vehicle)
            val detailFragment = VehicleDetailFragment()
            detailFragment.arguments = bundle
            (context as MainActivity).loadFragment(detailFragment)
        }
    }

    override fun getItemCount() = vehicleList.size

    // Called by search/filter (F5)
    fun updateList(newList: List<Vehicle>) {
        vehicleList = newList
        notifyDataSetChanged()
    }
}
```

#### `adapters/DriverAdapter.kt` — Same pattern:
- ViewHolder fields: `tvName`, `tvPhone`, `tvVehicle`, `tvAvailability`, `tvRating`, `viewStatusDot`
- Dot color: AVAILABLE=green, ON_ROUTE=orange, UNAVAILABLE=red
- Item click: Bundle with key `"DRIVER_DATA"`, loads `DriverDetailFragment`
- Has `updateList(newList: List<Driver>)` for search

---

### F4 — Fragment Transactions: Bottom Navigation (20 marks)

**`activity_main.xml` structure:**
```xml
<LinearLayout vertical>
    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu"/>
</LinearLayout>
```

**`res/menu/bottom_nav_menu.xml`:**
```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/nav_dashboard" android:title="Dashboard" android:icon="@drawable/ic_reports"/>
    <item android:id="@+id/nav_vehicles" android:title="Fleet" android:icon="@drawable/ic_vehicle"/>
    <item android:id="@+id/nav_drivers" android:title="Drivers" android:icon="@drawable/ic_driver"/>
</menu>
```

**Fragment switching rules:**
- Use `replace()` NOT `add()` — prevents fragment stacking
- Use `addToBackStack(null)` so back button works
- Default fragment on launch = `DashboardFragment` (loaded from MainActivity.onCreate)
- When bottom nav tab re-tapped, do NOT reload if already showing — check with `supportFragmentManager.findFragmentById(R.id.fragment_container)`

---

### F5 — Search / Filter on RecyclerView (part of RecyclerView marks)

**Implement in both `VehicleListFragment` and `DriverListFragment`:**

**In `VehicleListFragment.kt`:**
```kotlin
class VehicleListFragment : Fragment() {
    private lateinit var adapter: VehicleAdapter
    private lateinit var allVehicles: List<Vehicle>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allVehicles = DataSource.getVehicles()
        adapter = VehicleAdapter(requireContext(), allVehicles)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_vehicles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Search
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterVehicles(newText ?: "")
                return true
            }
        })

        // Filter chips (All / Active / Maintenance / Retired)
        setupFilterChips(view)
    }

    private fun filterVehicles(query: String) {
        val filtered = allVehicles.filter {
            it.registrationNumber.contains(query, ignoreCase = true) ||
            it.make.contains(query, ignoreCase = true) ||
            it.model.contains(query, ignoreCase = true) ||
            it.assignedDriver.contains(query, ignoreCase = true)
        }
        adapter.updateList(filtered)
    }

    private fun setupFilterChips(view: View) {
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_filter)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val filtered = when (checkedId) {
                R.id.rb_active -> allVehicles.filter { it.status == "ACTIVE" }
                R.id.rb_maintenance -> allVehicles.filter { it.status == "MAINTENANCE" }
                R.id.rb_retired -> allVehicles.filter { it.status == "RETIRED" }
                else -> allVehicles
            }
            adapter.updateList(filtered)
        }
    }
}
```

**Same pattern for `DriverListFragment` — filter by name, phone, vehicle, availability status.**

---

## COMPLETE LAYOUT FILES NEEDED

These are NEW or UPDATED layouts for Assignment 3. Reuse Assignment 2 drawables and values files.

| File | Description |
|------|-------------|
| `activity_splash.xml` | Splash screen — app logo centered, company name, "Loading..." text |
| `activity_main.xml` | Shell — FrameLayout (fragment container) + BottomNavigationView |
| `fragment_dashboard.xml` | Dashboard with owner name in toolbar, stats cards, welcome message |
| `fragment_vehicle_list.xml` | SearchView + RadioGroup filter + RecyclerView |
| `fragment_driver_list.xml` | SearchView + RecyclerView |
| `fragment_vehicle_detail.xml` | Full vehicle info — all Vehicle fields displayed |
| `fragment_driver_detail.xml` | Full driver info — all Driver fields displayed |
| `item_vehicle_card.xml` | RecyclerView row — reg number, make/model, status dot, driver, route |
| `item_driver_card.xml` | RecyclerView row — avatar, name, phone, vehicle, availability dot, rating |

### Key layout rules:
- `activity_main.xml` root = `LinearLayout` vertical (NOT CoordinatorLayout — that stays for fragment interiors)
- `fragment_container` is a `FrameLayout` with `layout_weight="1"` so it fills space above bottom nav
- Each fragment layout root = `ConstraintLayout` or `LinearLayout` — NOT an activity wrapper
- RecyclerView in list fragments: `android:layout_width="match_parent"`, `android:layout_height="0dp"`, `layout_weight="1"` so it fills remaining space below search/filter

---

## ANDROIDMANIFEST.XML — UPDATE REQUIRED

```xml
<application ...>
    <activity android:name=".activities.SplashActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN"/>
            <category android:name="android.intent.category.LAUNCHER"/>
        </intent-filter>
    </activity>

    <activity android:name=".activities.MainActivity"
        android:exported="false"/>

    <activity android:name=".activities.DetailActivity"
        android:exported="false"/>
</application>
```

**IMPORTANT:** `SplashActivity` is now the LAUNCHER, not `MainActivity`.

---

## LOGIC MAP — CONTENT FOR logic_map.pdf

The agent must output this table. Student converts to PDF.

| Requirement ID | Class / File | Function / Method | Implementation Description |
|---------------|-------------|-------------------|---------------------------|
| F1 (Intent) | `SplashActivity.kt` | `onCreate()` — `Handler.postDelayed()` | After 2s delay, creates Intent with `OWNER_NAME` and `COMPANY_NAME` extras, starts MainActivity, calls finish() to remove splash from back stack |
| F1 (Intent receive) | `MainActivity.kt` | `onCreate()` — `intent.getStringExtra()` | Receives owner name from Intent extra, puts it in Bundle, passes to DashboardFragment via `fragment.arguments` |
| F2 (Bundle) | `VehicleAdapter.kt` | `onBindViewHolder()` — `setOnClickListener` | On vehicle card tap, creates Bundle with `VEHICLE_DATA` key containing Serializable Vehicle object, attaches to VehicleDetailFragment, calls MainActivity.loadFragment() |
| F2 (Bundle receive) | `VehicleDetailFragment.kt` | `onViewCreated()` — `arguments?.getSerializable()` | Retrieves Vehicle object from arguments Bundle, populates all TextViews with vehicle fields |
| F2 (Bundle) | `DriverAdapter.kt` | `onBindViewHolder()` — `setOnClickListener` | Same pattern as Vehicle — Bundle with `DRIVER_DATA` key, loads DriverDetailFragment |
| F3 (RecyclerView) | `VehicleAdapter.kt` | `onCreateViewHolder()`, `onBindViewHolder()` | Inflates `item_vehicle_card.xml`, binds Vehicle data to ViewHolder views, sets status dot color programmatically |
| F3 (RecyclerView) | `DriverAdapter.kt` | `onCreateViewHolder()`, `onBindViewHolder()` | Inflates `item_driver_card.xml`, binds Driver data, sets availability dot color |
| F4 (Fragment) | `MainActivity.kt` | `loadFragment()`, `setupBottomNavigation()` | Bottom nav listener calls loadFragment() which does FragmentManager replace() with addToBackStack(). Three tabs: Dashboard, Fleet, Drivers |
| F4 (Fragment) | `DashboardFragment.kt` | `onViewCreated()` | Reads owner name from arguments Bundle, displays in toolbar subtitle |
| F5 (Search) | `VehicleListFragment.kt` | `filterVehicles()`, `setupFilterChips()` | SearchView.OnQueryTextListener filters list by reg/make/model/driver. RadioGroup listener filters by status. Both call adapter.updateList() |
| F5 (Search) | `DriverListFragment.kt` | `filterDrivers()` | SearchView filters by name, phone, vehicle. Calls adapter.updateList() |

---

## GRADING RUBRIC

| Criteria | Marks | What is Checked |
|----------|-------|-----------------|
| Intent & Navigation | 25 | Splash → Main via Intent with extras, data received correctly |
| RecyclerView | 25 | Both Vehicle and Driver lists with custom Adapter + ViewHolder, dynamic data |
| Fragment Management | 20 | Bottom nav switching fragments, back stack works, no activity restart |
| Data Communication | 20 | Bundle passes object to detail fragment, detail screen shows correct data |
| GitHub Collaboration | 10 | Commit history, all members contributed, proper commit messages |
| **Total** | **100** | |

---

## GITHUB REQUIREMENTS

- One repository per group, all members added as collaborators
- Each member must push at least their assigned screens/features
- Pull before starting work each session
- Commit message format: `feat: add VehicleAdapter with search filter` or `fix: bundle key mismatch in DetailFragment`
- Do NOT commit: `.gradle/`, `build/`, `.idea/`, `local.properties` — add these to `.gitignore`

---

## BUILD ORDER FOR AGENT

Follow this exact order:

1. Create `models/Vehicle.kt` and `models/Driver.kt`
2. Create `models/DataSource.kt` with all dummy data
3. Update `res/values/strings.xml` — add new strings for fragment titles, search hints, detail labels
4. Create all layout XML files (listed above) — reuse drawables from Assignment 2
5. Create `res/menu/bottom_nav_menu.xml`
6. Create `adapters/VehicleAdapter.kt`
7. Create `adapters/DriverAdapter.kt`
8. Create `fragments/DashboardFragment.kt`
9. Create `fragments/VehicleListFragment.kt`
10. Create `fragments/DriverListFragment.kt`
11. Create `fragments/VehicleDetailFragment.kt`
12. Create `fragments/DriverDetailFragment.kt`
13. Create `activities/SplashActivity.kt`
14. Update `activities/MainActivity.kt`
15. Update `AndroidManifest.xml`

---

## WHAT NOT TO DO

- Do NOT pass data using a companion object or static variable
- Do NOT use SharedPreferences to pass data between screens
- Do NOT use ListView — only RecyclerView
- Do NOT put business logic inside Activities — activities only contain navigation code
- Do NOT use `add()` for bottom nav switching — use `replace()`
- Do NOT forget `addToBackStack(null)` — without it, back button exits app instead of going back
- Do NOT hardcode vehicle/driver data directly in fragments — always read from DataSource
- Do NOT forget `Vehicle : Serializable` and `Driver : Serializable` — Bundle requires it
- Do NOT use `notifyItemChanged()` for search filter — use `notifyDataSetChanged()` on the full list swap
- Do NOT make SplashActivity the non-launcher after update — check Manifest carefully
