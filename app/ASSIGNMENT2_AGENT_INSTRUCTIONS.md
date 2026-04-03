# ASSIGNMENT2_AGENT_INSTRUCTIONS.md
# HOW TO BUILD THIS PROJECT — STEP BY STEP AGENT GUIDE
# Read ASSIGNMENT2_PRD.md FIRST, then follow this file exactly.

---

## STEP 0 — BEFORE YOU WRITE A SINGLE LINE

1. Read `ASSIGNMENT2_PRD.md` completely.
2. Confirm you understand: **NO Kotlin logic. NO raster images. NO hardcoded hex/strings/dimens in layouts.**
3. The build order is: Values files → Drawables → Layouts (simple to complex).

---

## STEP 1 — CREATE VALUES FILES FIRST

Create these three files before any layout or drawable.
All colors, dimensions, and strings are defined here once and referenced everywhere.

**Order:**
1. `res/values/colors.xml` — full contents in ASSIGNMENT2_PRD.md Section 6
2. `res/values/dimens.xml` — full contents in ASSIGNMENT2_PRD.md Section 6
3. `res/values/strings.xml` — create with all user-facing labels:
    - App name: "Drivo"
    - Screen titles: "Dashboard", "Fleet", "Drivers", "Vehicle Detail", "Settings"
    - Stats labels: "Vehicles", "Drivers", "Advances"
    - Table headers: "Part Name", "Action", "Date", "Cost (PKR)"
    - Filter labels: "All", "Active", "Maintenance", "Retired"
    - Settings labels: "Notifications", "Language", "Dark Mode", "Backup"
    - Settings subtitles: "Manage alerts and reminders", "English", "Off", "Last synced: Today"
    - Card labels: "Emergency Dispatch", "Fleet", "Drivers", "Salary", "Reports", "Settings"
    - Card subtitles: "12 vehicles", "18 active", "Mar 2025"
    - Tag cloud chips: "Engine Oil", "Brakes", "Tyres", "Battery", "Clutch", "Radiator", "Gearbox", "Air Filter", "Timing Belt", "Suspension"
    - Section headings: "Recent Vehicles", "Quick Actions", "Common Parts", "Maintenance History"
    - Dummy driver names: "Ali Hassan", "Usman Tariq", "Bilal Ahmed", "Kamran Sheikh"
    - Dummy phones: "+92 300 1234567", "+92 321 9876543", "+92 333 5551234", "+92 345 7890123"
    - Dummy vehicles: "LEA-1234", "LEB-5678", "LHR-4321", "ISB-9900", "KHI-0011"
    - Dummy routes: "Lahore Industrial Estate → Shahdara", "SITE Karachi → Korangi"
    - Dummy amounts: "PKR 25,000", "PKR 3,500", "PKR 8,500", "PKR 18,000"
    - Badge count: "3"
    - Stats numbers: "12", "18", "3"

4. `res/values/themes.xml`:
```xml
<resources>
    <style name="Theme.Drivo" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryVariant">@color/colorPrimaryDark</item>
        <item name="colorOnPrimary">@color/white</item>
        <item name="colorSecondary">@color/colorAccent</item>
        <item name="android:windowBackground">@color/colorBackground</item>
    </style>
</resources>
```

---

## STEP 2 — CREATE ALL DRAWABLE FILES

Create every drawable in `res/drawable/`. Full specs in ASSIGNMENT2_PRD.md Section 7.

**Simple shapes (create these first):**
- `bg_card.xml`
- `bg_card_red.xml` — same as bg_card but `<solid android:color="@color/colorError"/>`
- `bg_card_blue.xml` — same as bg_card but `<solid android:color="@color/colorPrimary"/>`, no stroke
- `bg_badge.xml`
- `bg_chip.xml`
- `bg_avatar_circle.xml`
- `bg_input_field.xml` — `<solid color=white/>`, `<corners radius=8dp/>`, `<stroke width=1dp color=@color/colorDivider/>`
- `bg_status_dot_green.xml` — oval, `@color/colorStatusActive`
- `bg_status_dot_orange.xml` — oval, `@color/colorStatusMaintenance`
- `bg_status_dot_red.xml` — oval, `@color/colorStatusRetired`

**Selector (state drawable):**
- `bg_radio_chip.xml` — see exact XML in ASSIGNMENT2_PRD.md Section 7

**Layer-list:**
- `ic_avatar.xml` — see exact XML in ASSIGNMENT2_PRD.md Section 7 (from assignment PDF)

**Vector drawables — create simple versions using `<vector>` with `<path>` elements:**

`ic_bell.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/white"
        android:pathData="M12,22c1.1,0 2,-0.9 2,-2h-4c0,1.1 0.9,2 2,2zM18,16v-5c0,-3.07 -1.64,-5.64 -4.5,-6.32V4c0,-0.83 -0.67,-1.5 -1.5,-1.5s-1.5,0.67 -1.5,1.5v0.68C7.63,5.36 6,7.92 6,11v5l-2,2v1h16v-1l-2,-2z"/>
</vector>
```

`ic_vehicle.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/colorPrimary"
        android:pathData="M4,16c0,0.88 0.39,1.67 1,2.22V20c0,0.55 0.45,1 1,1h1c0.55,0 1,-0.45 1,-1v-1h8v1c0,0.55 0.45,1 1,1h1c0.55,0 1,-0.45 1,-1v-1.78c0.61,-0.55 1,-1.34 1,-2.22V6c0,-3.5 -3.58,-4 -8,-4s-8,0.5 -8,4v10zM7.5,17c-0.83,0 -1.5,-0.67 -1.5,-1.5S6.67,14 7.5,14s1.5,0.67 1.5,1.5S8.33,17 7.5,17zM16.5,17c-0.83,0 -1.5,-0.67 -1.5,-1.5s0.67,-1.5 1.5,-1.5 1.5,0.67 1.5,1.5 -0.67,1.5 -1.5,1.5zM18,11L6,11V6h12v5z"/>
</vector>
```

`ic_driver.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/colorPrimary"
        android:pathData="M12,12c2.21,0 4,-1.79 4,-4s-1.79,-4 -4,-4 -4,1.79 -4,4 1.79,4 4,4zM12,14c-2.67,0 -8,1.34 -8,4v2h16v-2c0,-2.66 -5.33,-4 -8,-4z"/>
</vector>
```

`ic_salary.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/colorPrimary"
        android:pathData="M11.8,10.9c-2.27,-0.59 -3,-1.2 -3,-2.15 0,-1.09 1.01,-1.85 2.7,-1.85 1.78,0 2.44,0.85 2.5,2.1h2.21c-0.07,-1.72 -1.12,-3.3 -3.21,-3.81V3h-3v2.16c-1.94,0.42 -3.5,1.68 -3.5,3.61 0,2.31 1.91,3.46 4.7,4.13 2.5,0.6 3,1.48 3,2.41 0,0.69 -0.49,1.79 -2.7,1.79 -2.06,0 -2.87,-0.92 -2.98,-2.1h-2.2c0.12,2.19 1.76,3.42 3.68,3.83V21h3v-2.15c1.95,-0.37 3.5,-1.5 3.5,-3.55 0,-2.84 -2.43,-3.81 -4.7,-4.4z"/>
</vector>
```

`ic_dispatch.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/white"
        android:pathData="M7,2v11h3v9l7,-12h-4l4,-8z"/>
</vector>
```

`ic_reports.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/colorPrimary"
        android:pathData="M5,9.2h3V19L5,19zM10.6,5h2.8v14h-2.8zM16.2,13h2.8v6h-2.8z"/>
</vector>
```

`ic_settings.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/colorPrimary"
        android:pathData="M19.14,12.94c0.04,-0.3 0.06,-0.61 0.06,-0.94 0,-0.32 -0.02,-0.64 -0.07,-0.94l2.03,-1.58c0.18,-0.14 0.23,-0.41 0.12,-0.61l-1.92,-3.32c-0.12,-0.22 -0.37,-0.29 -0.59,-0.22l-2.39,0.96c-0.5,-0.38 -1.03,-0.7 -1.62,-0.94L14.4,2.81c-0.04,-0.24 -0.24,-0.41 -0.48,-0.41h-3.84c-0.24,0 -0.43,0.17 -0.47,0.41L9.25,5.35C8.66,5.59 8.12,5.92 7.63,6.29L5.24,5.33c-0.22,-0.08 -0.47,0 -0.59,0.22L2.74,8.87C2.62,9.08 2.66,9.34 2.86,9.48l2.03,1.58C4.84,11.36 4.8,11.69 4.8,12s0.02,0.64 0.07,0.94l-2.03,1.58c-0.18,0.14 -0.23,0.41 -0.12,0.61l1.92,3.32c0.12,0.22 0.37,0.29 0.59,0.22l2.39,-0.96c0.5,0.38 1.03,0.7 1.62,0.94l0.36,2.54c0.05,0.24 0.24,0.41 0.48,0.41h3.84c0.24,0 0.44,-0.17 0.47,-0.41l0.36,-2.54c0.59,-0.24 1.13,-0.56 1.62,-0.94l2.39,0.96c0.22,0.08 0.47,0 0.59,-0.22l1.92,-3.32c0.12,-0.22 0.07,-0.47 -0.12,-0.61L19.14,12.94zM12,15.6c-1.98,0 -3.6,-1.62 -3.6,-3.6 0,-1.98 1.62,-3.6 3.6,-3.6 1.98,0 3.6,1.62 3.6,3.6 0,1.98 -1.62,3.6 -3.6,3.6z"/>
</vector>
```

`ic_chevron_right.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/colorTextHint"
        android:pathData="M10,6L8.59,7.41 13.17,12l-4.58,4.59L10,18l6,-6z"/>
</vector>
```

`ic_search.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/colorTextSecondary"
        android:pathData="M15.5,14h-0.79l-0.28,-0.27C15.41,12.59 16,11.11 16,9.5 16,5.91 13.09,3 9.5,3S3,5.91 3,9.5 5.91,16 9.5,16c1.61,0 3.09,-0.59 4.23,-1.57l0.27,0.28v0.79l5,4.99L20.49,19l-4.99,-5zM9.5,14C7.01,14 5,11.99 5,9.5S7.01,5 9.5,5 14,7.01 14,9.5 11.99,14 9.5,14z"/>
</vector>
```

`ic_maintenance.xml`:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@color/colorPrimary"
        android:pathData="M13.78,4C13.78,4 13.28,4.78 13,6c-0.5,2 0,3 0,3s-1,-0.08 -2,-1c-0.75,-0.68 -1,-2 -1,-2S7,8 7,11c0,2.61 2.39,5 5,5s5,-2.39 5,-5C17,7.54 14.5,5 13.78,4zM12,14c-1.66,0 -3,-1.34 -3,-3 0,-1.1 0.4,-1.52 0.78,-2 0.2,0.57 0.55,1.06 1.06,1.5C11.54,11.12 12,11.5 12,12h1c0,-1 -0.38,-1.59 -0.78,-2.11C12.73,9.43 13,9 13,8.5c0.93,0.7 2,1.87 2,3.5 0,1.66 -1.34,3 -3,3z"/>
</vector>
```

---

## STEP 3 — CREATE LAYOUT FILES (ORDER MATTERS)

Build in this exact order. Earlier files are included/referenced by later ones.

### 3.1 Simple component layouts first

**`layout_notification_bell.xml`** (L5):
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="40dp"
    android:layout_height="40dp">

    <ImageView
        android:id="@+id/iv_bell"
        android:layout_width="@dimen/size_icon_medium"
        android:layout_height="@dimen/size_icon_medium"
        android:layout_gravity="center"
        android:src="@drawable/ic_bell"
        android:contentDescription="@string/notifications" />

    <TextView
        android:id="@+id/tv_badge"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="top|end"
        android:background="@drawable/bg_badge"
        android:text="@string/badge_count"
        android:textColor="@color/white"
        android:textSize="@dimen/text_size_badge"
        android:gravity="center"
        android:minWidth="@dimen/size_badge_min"
        android:minHeight="@dimen/size_badge_min"
        android:paddingStart="2dp"
        android:paddingEnd="2dp" />
</FrameLayout>
```

**`layout_settings_row.xml`** (L4):
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="@dimen/height_settings_row"
    android:paddingStart="@dimen/margin_screen"
    android:paddingEnd="@dimen/margin_screen"
    android:background="@color/colorSurface">

    <ImageView
        android:id="@+id/iv_icon"
        android:layout_width="@dimen/size_icon_medium"
        android:layout_height="@dimen/size_icon_medium"
        android:layout_alignParentStart="true"
        android:layout_centerVertical="true"
        android:src="@drawable/ic_settings"
        android:contentDescription="@string/settings" />

    <TextView
        android:id="@+id/tv_label"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_toEndOf="@id/iv_icon"
        android:layout_alignTop="@id/iv_icon"
        android:layout_marginStart="@dimen/margin_card"
        android:text="@string/label_notifications"
        android:textSize="@dimen/text_size_body1"
        android:textColor="@color/colorTextPrimary" />

    <TextView
        android:id="@+id/tv_subtitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/tv_label"
        android:layout_toEndOf="@id/iv_icon"
        android:layout_marginStart="@dimen/margin_card"
        android:text="@string/subtitle_notifications"
        android:textSize="@dimen/text_size_caption"
        android:textColor="@color/colorTextSecondary" />

    <ImageView
        android:id="@+id/iv_chevron"
        android:layout_width="@dimen/size_icon_small"
        android:layout_height="@dimen/size_icon_small"
        android:layout_alignParentEnd="true"
        android:layout_centerVertical="true"
        android:src="@drawable/ic_chevron_right"
        android:contentDescription="@null" />
</RelativeLayout>
```

**`layout_stats_bar.xml`** (L3):
- Root: `LinearLayout` horizontal, `android:weightSum="3"`, height=`@dimen/height_stats_bar`, background=`@color/colorSurface`
- 3 children each with `layout_weight="1"`, `layout_width="0dp"`, vertical, gravity=center
- Add vertical dividers between children
- Child 1: large `"12"` text in `@color/colorPrimary`, then `"Vehicles"` label in `@color/colorTextSecondary`
- Child 2: large `"18"` in `@color/colorPrimary`, label `"Drivers"`
- Child 3: large `"3"` in `@color/colorError`, label `"Advances"`

**`layout_filter_bar.xml`** (L9):
- Root: `RadioGroup`, horizontal, width=match_parent, padding=`@dimen/margin_screen`
- 4 RadioButtons: rb_all (checked=true), rb_active, rb_maintenance, rb_retired
- Each: `android:button="@null"`, background=`@drawable/bg_radio_chip`, gravity=center, padding 8dp × 16dp, `layout_weight="1"`, `layout_width="0dp"`, `layout_marginEnd="8dp"`

**`layout_parts_tag_cloud.xml`** (L10):
- Root: `ConstraintLayout`, padding=`@dimen/margin_screen`
- Add a section title TextView: "Common Parts", style H2
- Flow widget with `app:constraint_referenced_ids` listing all 10 chip IDs
- 10 TextViews with IDs chip_engine_oil through chip_suspension
- Each chip: background=`@drawable/bg_chip`, padding 6dp × 12dp, `wrap_content` width

**`layout_vehicle_carousel.xml`** (L8):
- Root: `HorizontalScrollView`, `scrollbars="none"`, height=`180dp`
- Child: `LinearLayout` horizontal, padding=`@dimen/margin_screen`
- 5 vehicle cards hardcoded, each 160dp wide with `bg_card.xml`, contains:
    - Registration TextView (bold, `@color/colorPrimary`)
    - Vehicle type TextView (secondary)
    - Row with status dot (10dp circle drawable) + status text

**`layout_maintenance_table.xml`** (L7):
- Root: `TableLayout`, width=match_parent, `stretchColumns="0,2"`, `shrinkColumns="1"`
- Row 0: header, background=`@color/colorTableHeader`, 4 cells with white bold text
- Rows 1–5: alternating `@color/colorRowAlt` / `@color/colorSurface`, 4 data cells each

**`item_driver_card.xml`** (L2):
- Root: `ConstraintLayout`, background=`@drawable/bg_card`, margin=`@dimen/margin_item`, padding=`@dimen/margin_card`
- Vertical Guideline at 22% — ID: `guideline_avatar`
- Avatar ImageView: `@drawable/ic_avatar`, 48dp circle using `bg_avatar_circle.xml` as background
- Name TextView (id: `tv_driver_name`), Phone TextView (id: `tv_driver_phone`) — both right of guideline
- Barrier (id: `barrier_name_phone`): `app:barrierDirection="bottom"`, references `tv_driver_name` and `tv_driver_phone`
- Vehicle chip, available chip, rating chip — in a horizontal Chain below the barrier

**`layout_dashboard_grid.xml`** (L6):
- Root: `GridLayout`, columnCount=2, padding=`@dimen/margin_screen`
- Card 1 (Emergency Dispatch): `layout_columnSpan="2"`, background=`@drawable/bg_card_red`, height=72dp
- Cards 2–6: each `layout_columnWeight="1"`, `layout_width="0dp"`, background=`@drawable/bg_card`

### 3.2 Screen layouts

**`activity_settings.xml`**:
- Root: `LinearLayout` vertical
- Toolbar at top
- ScrollView containing 4 instances of `layout_settings_row.xml` using `<include>` tags
- Each `<include>` must override the `tools:` attributes to show different labels

**`activity_vehicles.xml`**:
- Root: `LinearLayout` vertical
- Toolbar
- Include `layout_filter_bar.xml`
- ScrollView with 5 hardcoded driver cards using `<include layout="@layout/item_driver_card"/>`

**`activity_vehicle_detail.xml`**:
- Root: `NestedScrollView`
- Vehicle header card (reg number, type, status)
- Include `layout_maintenance_table.xml`
- Include `layout_parts_tag_cloud.xml`

**`activity_drivers.xml`**:
- Root: `LinearLayout` vertical
- Toolbar with search icon
- ScrollView with 4 `<include layout="@layout/item_driver_card"/>` — each with different `tools:` data

**`activity_driver_detail.xml`**:
- Root: `NestedScrollView`
- Large avatar header (use `ic_avatar.xml`)
- Driver info section
- Include `layout_stats_bar.xml` (attendance/dues/rating)

**`activity_main.xml`** (L1 — build last, most complex):
```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/app_bar"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:background="@color/colorPrimary">

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_scrollFlags="scroll|exitUntilCollapsed"
            app:contentScrim="@color/colorPrimary"
            app:title="@string/app_name">

            <!-- Stats bar shown only when expanded -->
            <include
                layout="@layout/layout_stats_bar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="bottom"
                app:layout_collapseMode="parallax"/>

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="pin">

                <!-- Include notification bell in toolbar -->
                <include layout="@layout/layout_notification_bell"/>

            </androidx.appcompat.widget.Toolbar>

        </com.google.android.material.appbar.CollapsingToolbarLayout>
    </com.google.android.material.appbar.AppBarLayout>

    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <!-- Section: Recent Vehicles (L8) -->
            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:padding="@dimen/margin_screen"
                android:text="@string/recent_vehicles"
                android:textSize="@dimen/text_size_h3"
                android:textColor="@color/colorTextPrimary"
                android:textStyle="bold"/>

            <include layout="@layout/layout_vehicle_carousel"/>

            <!-- Section: Quick Actions (L6) -->
            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:padding="@dimen/margin_screen"
                android:text="@string/quick_actions"
                android:textSize="@dimen/text_size_h3"
                android:textColor="@color/colorTextPrimary"
                android:textStyle="bold"/>

            <include layout="@layout/layout_dashboard_grid"/>

        </LinearLayout>
    </androidx.core.widget.NestedScrollView>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

---

## STEP 4 — VERIFY BEFORE FINISHING

Run through this checklist:

| Check | Pass? |
|-------|-------|
| L1 CoordinatorLayout in activity_main.xml with CollapsingToolbar | |
| L2 ConstraintLayout with Guideline + Barrier + Chain in item_driver_card.xml | |
| L3 LinearLayout with 3 weighted children in layout_stats_bar.xml | |
| L4 RelativeLayout using alignParentStart/End, toEndOf, below in layout_settings_row.xml | |
| L5 FrameLayout with badge using layout_gravity="top\|end" in layout_notification_bell.xml | |
| L6 GridLayout with columnSpan="2" on first card in layout_dashboard_grid.xml | |
| L7 TableLayout with stretchColumns="0,2" and 5+ rows in layout_maintenance_table.xml | |
| L8 HorizontalScrollView with 5 hardcoded cards in layout_vehicle_carousel.xml | |
| L9 RadioGroup with 4 RadioButtons, first checked by default in layout_filter_bar.xml | |
| L10 ConstraintLayout Flow with 10 chips in layout_parts_tag_cloud.xml | |
| Zero .png/.jpg/.webp files in project | |
| MainActivity.kt has ONLY setContentView | |
| All hex colors reference @color/ | |
| All strings reference @string/ | |
| All dimensions reference @dimen/ | |

---

## STEP 5 — WHAT NOT TO DO (AGENT MUST AVOID)

- Do NOT add `android:onClick` anywhere
- Do NOT add any import to MainActivity.kt
- Do NOT use `RecyclerView` (needs an Adapter)
- Do NOT use `ViewPager` (needs an Adapter)
- Do NOT add Retrofit, Room, or any library
- Do NOT use `@drawable/` that doesn't exist — create every drawable before referencing it
- Do NOT use `app:srcCompat` for vectors without checking the ImageView supports it — use `android:src` instead
- Do NOT put the Flow widget in a LinearLayout — it only works inside ConstraintLayout
- Do NOT forget `app:constraint_referenced_ids` on the Flow widget — without this, chips won't flow
- Do NOT use `layout_weight` in a `ConstraintLayout` — it only works in `LinearLayout`
- Do NOT forget `android:button="@null"` on RadioButtons when using custom background
