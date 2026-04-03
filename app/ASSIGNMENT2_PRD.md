# ASSIGNMENT2_PRD.md
# Drivo — Assignment 2: The XML Architect
# AI Agent Reference Document — READ THIS ENTIRE FILE BEFORE WRITING ANY CODE

---

## 1. WHAT THIS ASSIGNMENT IS

Build a **High-Fidelity UI Prototype** of Drivo using **Android XML layouts only**.
No backend. No Kotlin logic. No images. Just XML.

---

## 2. ABSOLUTE RULES — NEVER VIOLATE THESE

| Rule | Detail |
|------|--------|
| **ZERO Kotlin logic** | `MainActivity.kt` contains ONLY `setContentView(R.layout.activity_main)`. No `OnClickListener`, no `Adapter`, no `RecyclerView`, no variables. |
| **ZERO raster images** | No `.png`, `.jpg`, `.webp` anywhere in the project. Every icon, avatar, background must be an XML drawable (Shape, LayerList, or Vector). |
| **Hardcoded data only** | Use `android:text` or `tools:text` with realistic dummy data. Driver names, PKR amounts, registration numbers must look real. |
| **Colors in colors.xml** | No hardcoded hex strings inside layout XML files. Always reference `@color/name`. |
| **Strings in strings.xml** | No hardcoded English text in layout files. Always reference `@string/name`. Exception: `tools:text` for preview-only dummy data. |
| **Dimensions in dimens.xml** | No hardcoded `16dp`, `12sp` etc in layouts. Always reference `@dimen/name`. |
| **No hardcoded logic in Kotlin** | The grader will check — MainActivity.kt must stay empty. |

---

## 3. PROJECT CONTEXT (Use This for Dummy Data)

- **App:** Drivo — Pakistani transport company app
- **Owner manages:** buses/vehicles, drivers, industrial client contracts, salaries
- **Dummy vehicle reg numbers:** LEA-1234, LEB-5678, LHR-4321, ISB-9900, KHI-0011
- **Dummy driver names:** Ali Hassan, Usman Tariq, Bilal Ahmed, Kamran Sheikh, Farhan Malik
- **Dummy routes:** Lahore Industrial Estate → Shahdara, SITE Karachi → Korangi, Faisalabad Textile Mill → City Center
- **Dummy amounts:** PKR 25,000 / PKR 3,500 / PKR 12,000 / PKR 47,500
- **Dummy dates:** 15 Mar 2025, 02 Apr 2025, 28 Feb 2025
- **Dummy parts:** Engine Oil, Brake Pads, Tyres, Battery, Clutch Plate, Air Filter

---

## 4. MANDATORY LAYOUT REQUIREMENTS — ALL 10 MUST BE IMPLEMENTED

---

### L1 — CoordinatorLayout + CollapsingToolbarLayout
**File:** `activity_main.xml`
**What it must do:** Main dashboard. Toolbar shows "Drivo" + subtitle when expanded. Collapses to just the app name when content is scrolled up.

**Exact requirements:**
- Root = `androidx.coordinatorlayout.widget.CoordinatorLayout`
- Inside: `com.google.android.material.appbar.AppBarLayout`
- Inside AppBarLayout: `com.google.android.material.appbar.CollapsingToolbarLayout`
    - `app:layout_scrollFlags="scroll|exitUntilCollapsed"`
    - `app:contentScrim="@color/colorPrimary"`
    - `app:expandedTitleTextAppearance` — large white title
- Inside CollapsingToolbarLayout: `androidx.appcompat.widget.Toolbar`
    - `app:layout_collapseMode="pin"` so toolbar pins at top when collapsed
- The scrollable content below AppBarLayout must be a `NestedScrollView` with:
    - `app:layout_behavior="@string/appbar_scrolling_view_behavior"`
- The expanded area must contain the **L3 stats bar** and **L6 dashboard grid** (include them here as nested includes or inline)

---

### L2 — ConstraintLayout (Guidelines + Barrier + Chain)
**File:** `item_driver_card.xml`
**What it must do:** Driver profile card. Shows avatar, driver name, phone number, assigned vehicle, and an availability status badge.

**Exact requirements:**
- Root = `androidx.constraintlayout.widget.ConstraintLayout`
- **Guideline:** One vertical Guideline at `app:layout_constraintGuide_percent="0.22"` — avatar stays left of this line, all text starts right of it
- **Barrier:** One bottom Barrier referencing both `tv_driver_name` and `tv_driver_phone` — the vehicle assignment text is constrained to `app:layout_constraintTop_toBottomOf="@id/barrier_name_phone"` so it never overlaps regardless of text length
- **Chain:** Three views — `chip_available`, `chip_vehicle`, `chip_rating` — linked in a horizontal chain with `app:layout_constraintHorizontal_chainStyle="spread_inside"`
- Avatar: circular shape using `bg_avatar_circle.xml` drawable, size 48dp × 48dp
- Driver name: bold, `@style/TextAppearance.Drivo.H3`
- Phone: secondary color, `@style/TextAppearance.Drivo.Body2`

---

### L3 — LinearLayout with layout_weight
**File:** `layout_stats_bar.xml`
**What it must do:** Horizontal stats bar on the dashboard showing three equal-width metric tiles.

**Exact requirements:**
- Root = `LinearLayout`, `android:orientation="horizontal"`, `android:weightSum="3"`
- Exactly **3 child LinearLayouts**, each:
    - `android:layout_width="0dp"`
    - `android:layout_weight="1"`
    - `android:orientation="vertical"`
    - `android:gravity="center"`
- Child 1: number `"12"`, label `"Vehicles"`
- Child 2: number `"18"`, label `"Drivers"`
- Child 3: number `"3"`, label `"Advances"` — use `@color/colorError` for the number if > 0
- Dividers between children using `android:showDividers="middle"` and `android:divider`

---

### L4 — RelativeLayout (Settings Row)
**File:** `layout_settings_row.xml`
**What it must do:** Reusable settings row. Icon left, label + subtitle middle, chevron right.

**Exact requirements — these attribute names are MANDATORY for grading:**
- Root = `RelativeLayout`, `android:layout_height="64dp"`
- `iv_icon` (ImageView): `android:layout_alignParentStart="true"`, `android:layout_centerVertical="true"`, size 24dp
- `tv_label` (TextView): `android:layout_toEndOf="@id/iv_icon"`, `android:layout_alignTop="@id/iv_icon"`
- `tv_subtitle` (TextView): `android:layout_below="@id/tv_label"`, `android:layout_toEndOf="@id/iv_icon"`
- `iv_chevron` (ImageView): `android:layout_alignParentEnd="true"`, `android:layout_centerVertical="true"`
- Create 4 instances of this row inside `activity_settings.xml` with different dummy labels:
    - "Notifications" / "Manage alerts and reminders"
    - "Language" / "English"
    - "Dark Mode" / "Off"
    - "Backup" / "Last synced: Today"

---

### L5 — FrameLayout (Notification Badge)
**File:** `layout_notification_bell.xml`
**What it must do:** Bell icon with a red badge showing unread count overlaid on top-right.

**Exact requirements:**
- Root = `FrameLayout`, `android:layout_width="40dp"`, `android:layout_height="40dp"`
- Child 1: `iv_bell` — ImageView using `ic_bell.xml` vector, centered
- Child 2: `tv_badge` — TextView, `android:layout_gravity="top|end"`, `android:text="3"`, background = `bg_badge.xml` (red circle, see drawable specs), white text, size 10sp, min width/height 16dp, `android:gravity="center"`
- This component is **included inside the Toolbar** in `activity_main.xml` using `<include layout="@layout/layout_notification_bell"/>`

---

### L6 — GridLayout (Dashboard Cards with Column Span)
**File:** `layout_dashboard_grid.xml`
**What it must do:** 6 quick-action cards on the dashboard. Emergency Dispatch card spans 2 columns.

**Exact requirements:**
- Root = `GridLayout`
- `android:columnCount="2"`
- `android:useDefaultMargins="true"`
- Card 1 — **Emergency Dispatch** (RED): `android:layout_columnSpan="2"`, `android:layout_gravity="fill_horizontal"`, background `bg_card_red.xml`, white text, icon `ic_dispatch.xml`
- Card 2 — Fleet: icon `ic_vehicle.xml`, label "Fleet", subtitle "12 vehicles"
- Card 3 — Drivers: icon `ic_driver.xml`, label "Drivers", subtitle "18 active"
- Card 4 — Salary: icon `ic_salary.xml`, label "Salary", subtitle "Mar 2025"
- Card 5 — Reports: icon `ic_reports.xml`, label "Reports"
- Card 6 — Settings: icon `ic_settings.xml`, label "Settings"
- Cards 2–6 each: background `bg_card.xml`, `android:layout_width="0dp"`, `android:layout_columnWeight="1"`

---

### L7 — TableLayout (Maintenance History)
**File:** `layout_maintenance_table.xml`
**What it must do:** Data table showing recent maintenance log for a vehicle. 4 columns: Part | Action | Date | Cost

**Exact requirements:**
- Root = `TableLayout`
- **`android:stretchColumns="0,2"`** — Part Name and Date columns stretch to fill width
- `android:shrinkColumns="1"` — Action column can shrink
- Row 0 (header): background `@color/colorPrimary`, all text white, bold
    - Cells: "Part Name" | "Action" | "Date" | "Cost (PKR)"
- Rows 1–5 (data): alternate background — odd rows `@color/colorRowAlt` (`#F0F5FB`), even rows `@color/colorSurface` (`#FFFFFF`)
- Hardcoded data rows:
    1. Engine Oil | Replaced | 15 Mar 2025 | 2,500
    2. Brake Pads | Repaired | 02 Apr 2025 | 4,800
    3. Battery | Replaced | 28 Feb 2025 | 8,500
    4. Air Filter | Inspected | 10 Jan 2025 | 500
    5. Tyres (x2) | Replaced | 05 Dec 2024 | 18,000
- Each cell: `android:padding="@dimen/cell_padding"` (8dp)

---

### L8 — HorizontalScrollView (Vehicle Carousel)
**File:** `layout_vehicle_carousel.xml`
**What it must do:** Horizontal scrolling row of vehicle summary cards on the dashboard.

**Exact requirements:**
- Root = `HorizontalScrollView`
    - `android:scrollbars="none"`
    - `android:fillViewport="false"`
- Direct child = `LinearLayout`, `android:orientation="horizontal"`, `android:padding="@dimen/margin_screen"`
- Must contain exactly **5 vehicle cards** hardcoded, each:
    - `android:layout_width="160dp"`, `android:layout_height="wrap_content"`
    - `android:layout_marginEnd="@dimen/margin_item"`
    - background = `bg_card.xml`
    - `android:padding="@dimen/margin_card"`
    - Contains: registration TextView (bold), vehicle type TextView, status dot View (colored circle 10dp)
- Vehicle data:
    1. LEA-1234 | Coaster | ACTIVE (green dot)
    2. LEB-5678 | Bus | ACTIVE (green dot)
    3. LHR-4321 | Hi-Ace | MAINTENANCE (orange dot)
    4. ISB-9900 | Coaster | ACTIVE (green dot)
    5. KHI-0011 | Bus | RETIRED (red dot)

---

### L9 — RadioGroup (Fleet Filter Bar)
**File:** `layout_filter_bar.xml`
**What it must do:** Filter selection on the Vehicles screen. Single-select filter.

**Exact requirements:**
- Root = `RadioGroup`
    - `android:orientation="horizontal"`
    - `android:layout_width="match_parent"`
- Contains exactly **4 RadioButtons:**
    1. `rb_all` — text "All", `android:checked="true"`
    2. `rb_active` — text "Active"
    3. `rb_maintenance` — text "Maintenance"
    4. `rb_retired` — text "Retired"
- Each RadioButton: background = `@drawable/bg_radio_chip` (see drawable specs), no default radio circle button visual (`android:button="@null"`), `android:gravity="center"`, padding 8dp horizontal
- `bg_radio_chip.xml` is a `<selector>` with:
    - `state_checked="true"`: filled `@color/colorPrimary` + white text (text color set via `ColorStateList`)
    - default: stroke `@color/colorPrimary` + transparent fill + primary text color
- Include this in `activity_vehicles.xml`

---

### L10 — ConstraintLayout Flow (Tag Cloud)
**File:** `layout_parts_tag_cloud.xml`
**What it must do:** A tag cloud of maintenance part categories that wraps to next line automatically.

**Exact requirements:**
- Root = `androidx.constraintlayout.widget.ConstraintLayout`
- Contains a `androidx.constraintlayout.helper.widget.Flow`
    - `app:flow_wrapMode="chain"`
    - `app:flow_horizontalGap="8dp"`
    - `app:flow_verticalGap="8dp"`
    - `app:flow_horizontalStyle="packed"`
    - `app:flow_horizontalBias="0"` — left-aligned
    - `app:constraint_referenced_ids` lists all chip view IDs
- Contains **10 TextView chips**, each:
    - background = `bg_chip.xml` (rounded rectangle, light blue fill, blue stroke)
    - padding 6dp vertical, 12dp horizontal
    - `android:layout_width="wrap_content"`
- Chip labels: "Engine Oil", "Brakes", "Tyres", "Battery", "Clutch", "Radiator", "Gearbox", "Air Filter", "Timing Belt", "Suspension"
- Include this in `activity_vehicle_detail.xml` under a "Common Parts" section heading

---

## 5. COMPLETE FILE LIST — CREATE ALL OF THESE

### Layout Files (`res/layout/`)
| Filename | Description |
|----------|-------------|
| `activity_main.xml` | Dashboard with L1 CoordinatorLayout |
| `activity_vehicles.xml` | Vehicle list screen with L9 filter bar |
| `activity_vehicle_detail.xml` | Vehicle detail with L7 table and L10 tag cloud |
| `activity_drivers.xml` | Driver list screen |
| `activity_driver_detail.xml` | Driver profile with L2 driver card |
| `activity_settings.xml` | Settings screen with L4 rows |
| `layout_stats_bar.xml` | L3 weighted stats bar |
| `layout_dashboard_grid.xml` | L6 grid of action cards |
| `layout_vehicle_carousel.xml` | L8 horizontal vehicle scroll |
| `layout_maintenance_table.xml` | L7 maintenance history table |
| `layout_notification_bell.xml` | L5 notification badge |
| `layout_settings_row.xml` | L4 reusable settings row |
| `layout_filter_bar.xml` | L9 radio group filter |
| `layout_parts_tag_cloud.xml` | L10 Flow tag cloud |
| `item_driver_card.xml` | L2 driver card (used in activity_drivers.xml — hardcode 4 of these stacked in a ScrollView) |

### Drawable Files (`res/drawable/`)
| Filename | Type | Description |
|----------|------|-------------|
| `bg_card.xml` | Shape | White fill, 12dp corners, 1dp #E0E0E0 stroke |
| `bg_card_red.xml` | Shape | #D32F2F fill, 12dp corners, no stroke |
| `bg_card_blue.xml` | Shape | #1E5C9B fill, 12dp corners, no stroke |
| `bg_badge.xml` | Shape | #F44336 (red) oval fill, no stroke — for notification dot |
| `bg_radio_chip.xml` | Selector | checked=filled blue; default=outlined blue |
| `bg_chip.xml` | Shape | #E3F0FB fill, #1E5C9B stroke 1dp, 16dp corners |
| `bg_avatar_circle.xml` | Shape | Oval, @color/colorPrimary fill |
| `bg_status_dot_green.xml` | Shape | Oval, #4CAF50 fill |
| `bg_status_dot_orange.xml` | Shape | Oval, #FF9800 fill |
| `bg_status_dot_red.xml` | Shape | Oval, #F44336 fill |
| `bg_input_field.xml` | Shape | White fill, 8dp corners, 1dp #CCCCCC stroke |
| `ic_avatar.xml` | LayerList | Human silhouette (use the LayerList from assignment PDF exactly) |
| `ic_vehicle.xml` | Vector | Simple bus/vehicle shape (rectangle + wheels using paths) |
| `ic_driver.xml` | Vector | Person icon |
| `ic_salary.xml` | Vector | Banknote / PKR icon |
| `ic_dispatch.xml` | Vector | Lightning bolt or alert icon |
| `ic_reports.xml` | Vector | Bar chart icon |
| `ic_settings.xml` | Vector | Gear icon |
| `ic_bell.xml` | Vector | Bell icon |
| `ic_chevron_right.xml` | Vector | Right arrow > |
| `ic_maintenance.xml` | Vector | Wrench icon |
| `ic_search.xml` | Vector | Magnifying glass |

---

## 6. VALUES FILES — COMPLETE CONTENTS

### `res/values/colors.xml`
```xml
<resources>
    <color name="colorPrimary">#1E5C9B</color>
    <color name="colorPrimaryDark">#1E3A5F</color>
    <color name="colorPrimaryLight">#4A90D9</color>
    <color name="colorAccent">#FF8C00</color>
    <color name="colorBackground">#F5F7FA</color>
    <color name="colorSurface">#FFFFFF</color>
    <color name="colorError">#D32F2F</color>
    <color name="colorWarning">#F57C00</color>
    <color name="colorSuccess">#388E3C</color>
    <color name="colorTextPrimary">#1A1A1A</color>
    <color name="colorTextSecondary">#555555</color>
    <color name="colorTextHint">#999999</color>
    <color name="colorDivider">#E0E0E0</color>
    <color name="colorStatusActive">#4CAF50</color>
    <color name="colorStatusMaintenance">#FF9800</color>
    <color name="colorStatusRetired">#F44336</color>
    <color name="colorRowAlt">#F0F5FB</color>
    <color name="colorCardStroke">#E0E0E0</color>
    <color name="colorBadge">#F44336</color>
    <color name="colorChipFill">#E3F0FB</color>
    <color name="colorChipStroke">#1E5C9B</color>
    <color name="colorTableHeader">#1E5C9B</color>
    <color name="white">#FFFFFF</color>
    <color name="black">#000000</color>
</resources>
```

### `res/values/dimens.xml`
```xml
<resources>
    <dimen name="margin_screen">16dp</dimen>
    <dimen name="margin_card">12dp</dimen>
    <dimen name="margin_item">8dp</dimen>
    <dimen name="corner_radius_card">12dp</dimen>
    <dimen name="corner_radius_button">8dp</dimen>
    <dimen name="corner_radius_chip">16dp</dimen>
    <dimen name="elevation_card">2dp</dimen>
    <dimen name="size_icon_small">20dp</dimen>
    <dimen name="size_icon_medium">24dp</dimen>
    <dimen name="size_icon_large">48dp</dimen>
    <dimen name="size_avatar">48dp</dimen>
    <dimen name="size_status_dot">10dp</dimen>
    <dimen name="size_badge_min">16dp</dimen>
    <dimen name="height_settings_row">64dp</dimen>
    <dimen name="height_stats_bar">80dp</dimen>
    <dimen name="width_vehicle_card">160dp</dimen>
    <dimen name="cell_padding">8dp</dimen>
    <dimen name="text_size_h1">24sp</dimen>
    <dimen name="text_size_h2">20sp</dimen>
    <dimen name="text_size_h3">17sp</dimen>
    <dimen name="text_size_body1">15sp</dimen>
    <dimen name="text_size_body2">13sp</dimen>
    <dimen name="text_size_caption">12sp</dimen>
    <dimen name="text_size_badge">10sp</dimen>
    <dimen name="text_size_stat_number">22sp</dimen>
</resources>
```

---

## 7. EXACT DRAWABLE SPECS (XML CODE)

### `bg_card.xml`
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/colorSurface"/>
    <corners android:radius="@dimen/corner_radius_card"/>
    <stroke android:width="1dp" android:color="@color/colorCardStroke"/>
</shape>
```

### `bg_badge.xml`
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="@color/colorBadge"/>
</shape>
```

### `bg_chip.xml`
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/colorChipFill"/>
    <corners android:radius="@dimen/corner_radius_chip"/>
    <stroke android:width="1dp" android:color="@color/colorChipStroke"/>
</shape>
```

### `bg_avatar_circle.xml`
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="@color/colorPrimary"/>
</shape>
```

### `bg_radio_chip.xml`
```xml
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_checked="true">
        <shape>
            <solid android:color="@color/colorPrimary"/>
            <corners android:radius="@dimen/corner_radius_chip"/>
        </shape>
    </item>
    <item>
        <shape>
            <solid android:color="@color/colorSurface"/>
            <corners android:radius="@dimen/corner_radius_chip"/>
            <stroke android:width="1dp" android:color="@color/colorPrimary"/>
        </shape>
    </item>
</selector>
```

### `ic_avatar.xml` (from assignment PDF — use exactly)
```xml
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape android:shape="oval">
            <solid android:color="@color/colorPrimary"/>
        </shape>
    </item>
    <item android:top="15dp" android:bottom="45dp" android:left="30dp" android:right="30dp">
        <shape android:shape="oval">
            <solid android:color="@color/white"/>
        </shape>
    </item>
    <item android:top="50dp" android:left="10dp" android:right="10dp" android:bottom="-10dp">
        <shape android:shape="oval">
            <solid android:color="@color/white"/>
        </shape>
    </item>
</layer-list>
```

---

## 8. GRADING RUBRIC — HOW MARKS ARE ALLOCATED

| Criteria | Marks | What the Grader Checks |
|----------|-------|------------------------|
| Layout Coverage (L1–L10) | 40 | ALL 10 layouts present and correctly implemented |
| Design Map | 20 | `design_map.pdf` accurately maps each L# to the right file and explains its function |
| Visual Polish | 15 | Consistent padding/margins, no squashed views, colors from colors.xml |
| XML Assets | 15 | Zero raster images, complex shapes built as XML drawables |
| Code Hygiene | 10 | Files named clearly, MainActivity.kt is empty |

**Total: 100 points**

---

## 9. SUBMISSION CHECKLIST

- [ ] `app/src/main/res/layout/` folder — all layout XML files
- [ ] `app/src/main/res/drawable/` folder — all drawable XML files
- [ ] `design_map.pdf` — mapping document (see Section 10 below)
- [ ] `MainActivity.kt` contains ONLY `setContentView(R.layout.activity_main)`
- [ ] Zero `.png`, `.jpg`, `.webp` files in project
- [ ] All colors reference `@color/`, all strings reference `@string/`, all dimensions reference `@dimen/`

---

## 10. DESIGN MAP — CONTENT FOR design_map.pdf

The agent must generate this table. It will be converted to PDF separately.

**Roll No:** [Student fills in] | **Project:** Drivo — Transport Fleet App

| Req ID | Screen / File | Function in Drivo |
|--------|--------------|-------------------------------|
| L1 (CoordinatorLayout) | `activity_main.xml` | Main dashboard. The stats bar and company info header collapses when the user scrolls down through the vehicle carousel and action cards. |
| L2 (ConstraintLayout) | `item_driver_card.xml` | Driver profile card. Guideline anchors avatar column. Barrier below name/phone prevents vehicle assignment text from overlapping on long names. Chain distributes status chips evenly. |
| L3 (LinearLayout Weight) | `layout_stats_bar.xml` | Dashboard stats row showing Active Vehicles, Drivers, and Pending Advances. Weights force equal width on all three tiles regardless of number size. |
| L4 (RelativeLayout) | `layout_settings_row.xml` | Reusable settings row used in `activity_settings.xml`. Icon aligned to start, label and subtitle in middle, chevron aligned to end using RelativeLayout anchors. |
| L5 (FrameLayout) | `layout_notification_bell.xml` | Bell icon in the dashboard toolbar. A red badge showing unread alert count is stacked on top-right of the bell using FrameLayout and layout_gravity. |
| L6 (GridLayout) | `layout_dashboard_grid.xml` | 6 quick-action cards on dashboard. Emergency Dispatch card spans both columns (layout_columnSpan=2) to give it priority visual weight. |
| L7 (TableLayout) | `layout_maintenance_table.xml` | Maintenance history table on vehicle detail screen. stretchColumns="0,2" makes Part Name and Date columns fill available width. |
| L8 (HorizontalScrollView) | `layout_vehicle_carousel.xml` | Horizontal scrolling row of 5 vehicle summary cards on dashboard. Scrolls independently of the main vertical scroll. |
| L9 (RadioGroup) | `layout_filter_bar.xml` | Filter bar on vehicles screen. Single-select filter for All / Active / Maintenance / Retired status. RadioButtons styled as chips. |
| L10 (Flow) | `layout_parts_tag_cloud.xml` | Tag cloud of 10 maintenance part categories on vehicle detail screen. Flow widget wraps chips to next line automatically when they exceed screen width. |
